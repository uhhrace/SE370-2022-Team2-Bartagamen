package com.example.myapplication;

import java.time.Instant;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class PetScreenController extends BartScreenController {

    // Avoid hard-coded values, declare here
    final int DAYS_PER_WEEK = 7;
    final int SECONDS_PER_DAY = 86400;

    DAO dao;
    DailyMealPlanEngine dmp;

    ToggleButton sunButton;
    ToggleButton monButton;
    ToggleButton tueButton;
    ToggleButton wedButton;
    ToggleButton thuButton;
    ToggleButton friButton;
    ToggleButton satButton;
    ToggleButton[] dayButtons;
    AppCompatButton monthButton;
    AppCompatButton editPetButton;
    int currentDay;
    MealPlan currentMealPlan;

    int DISPLAYED_PET_ID;

    TextView dateView, countItems, mealPlanDisplay;
    Date currentDate;
    CharSequence selectedDateString;

    public PetScreenController(){
        dmp = DailyMealPlanEngine.getDMPEngine();
        dao = DAO.getDAO();

        dayButtons = new ToggleButton[DAYS_PER_WEEK];
        currentDate = new Date();
        selectedDateString = DateFormat.format("MMMM d, yyyy ", currentDate.getTime());
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pet_screen, container, false);

        dateView = view.findViewById(R.id.dateField);
        countItems = view.findViewById(R.id.countDailyFoodItems);
        mealPlanDisplay = view.findViewById(R.id.petScreenMealPlanDisplay);

        try
        {
            countItems.setText("Daily Food Items: " + currentMealPlan.getFoodIdList().size());
        }
        catch(final Exception e)
        {
            // Do nothing
        }

        dateView.setText("Date: "+ selectedDateString);

        setDayButtonListeners(view);

        setMonthButtonListener(view);

        setEditPetButtonListener();

        autoSelectDayOfWeek();

        //TODO low priority
        // check button sizes, if < 100dp, change SUN -> S, MON -> M, etc

        return view;
    }

    public void setDisplayedPetId(int newId){
        DISPLAYED_PET_ID = newId;
    }

    void autoSelectDayOfWeek(){
        // check current day of the week, highlight current day
        //Calendar calendar = Calendar.getInstance();

        //The Calendar.DAY_OF_WEEK enum starts with Mon at position 1, ends with Sun at position 7
        currentDay = currentDate.getDay();
        //Decrement the offset to match with our array
        dayButtons[currentDay].performClick();
    }

    void setMonthButtonListener(View view){

        // Assign buttons
        monthButton = view.findViewById(R.id.ViewMonthlyButton);

        monthButton.setOnClickListener(tempView -> changeScreenToCalendar());
    }

    void setEditPetButtonListener() {

        Menu topMenu = main.getTopMenu();
        // Only one item, pull first item
        MenuItem editPetButton = topMenu.getItem(0);
        editPetButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                changeScreenToEditPet();
                return true;
            }
        });
    }

    void setDayButtonListeners(View view){
        // Assign buttons
        sunButton = view.findViewById(R.id.sunButton);
        monButton = view.findViewById(R.id.monButton);
        tueButton = view.findViewById(R.id.tueButton);
        wedButton = view.findViewById(R.id.wedButton);
        thuButton = view.findViewById(R.id.thuButton);
        friButton = view.findViewById(R.id.friButton);
        satButton = view.findViewById(R.id.satButton);

        // Fill button array
        dayButtons[0] = sunButton;
        dayButtons[1] = monButton;
        dayButtons[2] = tueButton;
        dayButtons[3] = wedButton;
        dayButtons[4] = thuButton;
        dayButtons[5] = friButton;
        dayButtons[6] = satButton;

        // For each button
        for(ToggleButton button : dayButtons){
            // We could keep track of the last button clicked, and only reset that button instead
            // of resetting all the buttons if we end up needing better performance here. I'm not
            // sure how that would work if the user presses multiple buttons at the same time.
            button.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View buttonClicked) {

                    // Reset every other button to unpressed colors
                    for(ToggleButton butt : dayButtons){
                        butt.setBackground(getResources().getDrawable(R.drawable.rounded_corner_inactive));
                        butt.setTextColor( getResources().getColor(R.color.buttonDarkBlue));
                    }
                    // Set clicked button to pressed colors
                    button.setBackground(getResources().getDrawable(R.drawable.rounded_corner_active));
                    button.setTextColor(getResources().getColor(R.color.white));

                    findDateAndRequestMenu(button);

                    try{
                        updateMealPlanDisplay();
                    }catch (JSONException | NullPointerException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    } // end setDayButtonListeners

    void updateMealPlanDisplay() throws JSONException, NullPointerException {

        ArrayList<Integer> foodIds;
        JSONArray foodList = dao.getFoodList();
        String mealPlanText = "";

        try{
            foodIds = currentMealPlan.getFoodIdList();
        }catch (NullPointerException e){
            //no meal plan for selected date, initialize to size 0 to skip the upcoming for loop
            foodIds = new ArrayList<>();
            mealPlanText = "No Meal Plan recorded for selected date";
        }

        for(int i = 0; i < foodIds.size(); i++){

            mealPlanText += foodList.getJSONObject(foodIds.get(i)-1).get("type").toString();
            mealPlanText += "\n\t";
            mealPlanText += foodList.getJSONObject(foodIds.get(i)-1).get("name").toString();
            mealPlanText += "\n";
        }

        mealPlanDisplay.setText(mealPlanText);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void findDateAndRequestMenu(ToggleButton button){
        Date selectedDate = findDateOfSelectedDayButton(button);

        selectedDateString  = DateFormat.format("MMMM d, yyyy ", selectedDate.getTime());
        dateView.setText("" + selectedDateString);

        try{
            // Send a request to MenuDAO requesting the menu for selectedDate and selectedPet
            currentMealPlan = dao.getMealPlan(
                    DISPLAYED_PET_ID,
                    new Date(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate())
            );
        }catch (JSONException e){
            currentMealPlan = dmp.generateMealPlanForPetOnDate(
                    DISPLAYED_PET_ID,
                    new Date(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate())
            );
        }

        try{
            countItems.setText("Daily Food Items: " + currentMealPlan.getFoodIdList().size());
        }catch (NullPointerException e){
            // No meal plan set
            countItems.setText("Daily Food Items: None");
        }

        try{
            updateMealPlanDisplay();
        }catch (JSONException e){
            e.printStackTrace();
            mealPlanDisplay.setText("JSON Error. Clear app cache.");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    Date findDateOfSelectedDayButton(ToggleButton button){
        // Find index of selected button
        int selectedDay = Arrays.asList(dayButtons).indexOf(button);

        // Find the difference in days between today and selected day
        int dayDiff = selectedDay - currentDay;

        // Convert difference from days -> seconds
        Instant now = Instant.now();
        int secondsDiff = dayDiff * SECONDS_PER_DAY;
        // Create an instant at the selected day
        Instant then = now.plusSeconds(secondsDiff);

        // Return the date of the selected day
        return Date.from(then);
    }
}
