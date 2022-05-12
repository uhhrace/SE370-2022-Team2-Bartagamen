package com.example.myapplication;

import java.time.Instant;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

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
    int currentDay;
    DailyMealPlanEngine.MealPlan currentMealPlan;

    int DISPLAYED_PET_ID;

    TextView dateView, countItems;
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

       // String countFoodItems = getArguments().getString("count");

        View view = inflater.inflate(R.layout.pet_screen, container, false);

        dateView = view.findViewById(R.id.dateField);
        countItems = view.findViewById(R.id.countDailyFoodItems);
        final Bundle bdl = getArguments();

        //get Data from Daily Meal Plan Engine
        String str = "";
        try
        {
            str = bdl.getString("count");
            countItems.setText("Daily Food Items: "+str);
        }
        catch(final Exception e)
        {
            // Do nothing
        }

        dateView.setText("Date: "+ selectedDateString);

        setDayButtonListeners(view);

        setMonthButtonListener(view);

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
                        updateMealPlanDisplay(view);
                    }catch (JSONException | NullPointerException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    } // end setDayButtonListeners

    void updateMealPlanDisplay(View view) throws JSONException, NullPointerException {

        TextView mealPlanDisplay = view.findViewById(R.id.petScreenMealPlanDisplay);

        ArrayList<Integer> foodIds = currentMealPlan.getFoodIdList();
        FoodItem[] plannedFoods = new FoodItem[foodIds.size()];
        JSONArray foodList = dao.getFoodList();

        String mealPlanText = "";

        for(int i = 0; i < foodIds.size(); i++){

            mealPlanText += foodList.getJSONObject(foodIds.get(i)-1).get("type").toString();
            mealPlanText += "\n\t";
            mealPlanText += foodList.getJSONObject(foodIds.get(i)-1).get("name").toString();
            mealPlanText += "\n";

//            plannedFoods[i] = new FoodItem(
//                    (int) foodList.getJSONObject(i).get("id"),
//                    (DailyMealPlanEngine.FoodType) foodList.getJSONObject(i).get("type"),
//                     foodList.getJSONObject(i).get("name").toString());
        }

        mealPlanDisplay.setText(mealPlanText);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void findDateAndRequestMenu(ToggleButton button){
        Date selectedDate = findDateOfSelectedDayButton(button);

        selectedDateString  = DateFormat.format("MMMM d, yyyy ", selectedDate.getTime());
        dateView.setText("" + selectedDateString);

        // TODO High Priority Feature
        //  Request a specific date here
        currentMealPlan = dmp.generateMealPlanForPetToday(DISPLAYED_PET_ID);

        //TODO High Priority Feature
        // Send a request to MenuDAO requesting the menu for selectedDate and selectedPet
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
