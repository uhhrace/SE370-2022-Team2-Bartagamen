package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarScreenController extends BartScreenController {

    CalendarView calendarView;
    TextView selectedDayMenuTextView;
    static final String TAG = "CalActivity";
    public String dateString;
    Calendar selectedDate;
    DAO dao;
    DailyMealPlanEngine dmp;

    public CalendarScreenController(){
        selectedDate = Calendar.getInstance();
        dao = DAO.getDAO();
        dmp = DailyMealPlanEngine.getDMPEngine();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.calendar_screen, container, false);

        setCalendarViewListener(view);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void updateMealPlanDisplay(Date selectedDate) throws JSONException{

        //Create MealPlan for selected date
        MealPlan selectedDateMealPlan = null;

        //Truncate time off selectedDate
        selectedDate = Date.from(selectedDate.toInstant());

        // Pull MealPlan from DB
        try{
            selectedDateMealPlan = dao.getMealPlan(1, selectedDate);
        }catch (JSONException e){
            e.printStackTrace();
            //We don't need to do this anymore, getMealPlan already has logic to check requestedDate
            //  and build a new meal plan if necessary
            //selectedDateMealPlan = dmp.generateMealPlanForPetOnDate(1, selectedDate);
        }

        ArrayList<Integer> foodIds;
        JSONArray foodList = dao.getFoodList();
        String mealPlanText = "";

        try{
            foodIds = selectedDateMealPlan.getFoodIdList();
        }catch (NullPointerException e){
            //no meal plan for selected date
            foodIds = new ArrayList<>();
            mealPlanText = "No Meal Plan recorded for selected date";
        }

        for(int i = 0; i < foodIds.size(); i++){

            mealPlanText += foodList.getJSONObject(foodIds.get(i)-1).get("type").toString();
            mealPlanText += "\n\t";
            mealPlanText += foodList.getJSONObject(foodIds.get(i)-1).get("name").toString();
            mealPlanText += "\n";
        }

        // Update TextView notifying the user they selected the correct Date,
        //  and display the meal plan for that date
        selectedDayMenuTextView.setText("Menu for " + dateString + ":\n\n" + mealPlanText);
    }

    void setCalendarViewListener(View view){

        calendarView = view.findViewById(R.id.calendarView);
        selectedDayMenuTextView = view.findViewById(R.id.selectedDayMenu);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                //Build date string with selected day
                dateString = (month + 1) + "/" + day + "/" + year;
                Log.d(TAG, "OnSelectedDayChange: MMMM d, yyyy: "+ dateString);

                // Build Date object with selected day
                selectedDate.set(year, month, day);

                //Strip time elements
                selectedDate.set(Calendar.HOUR_OF_DAY, 0);
                selectedDate.set(Calendar.MINUTE, 0);
                selectedDate.set(Calendar.SECOND, 0);
                selectedDate.set(Calendar.MILLISECOND, 0);

                try{
                    // Send a request to MenuDAO for the menu for selectedDate and selectedPet
                    updateMealPlanDisplay(selectedDate.getTime());
                }catch (JSONException e){
                    e.printStackTrace();
                    selectedDayMenuTextView.setText("Catastrophic database error");
                }
            }
        });
    }
}
