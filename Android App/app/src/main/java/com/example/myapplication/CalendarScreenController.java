package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;

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

    void updateMealPlanDisplay(View view, Date selectedDate) throws JSONException{

        //Create MealPlan for selected date
        MealPlan selectedDateMealPlan = null;


        // Pull MealPlan from DB, or generate a new one if necessary
        try{
            selectedDateMealPlan = dao.getMealPlan(1, selectedDate);
        }catch (JSONException e){
            e.printStackTrace();
            //We don't need to do this anymore, getMealPlan already has logic to check requestedDate
            //  and build a new meal plan if necessary
            //selectedDateMealPlan = dmp.generateMealPlanForPetOnDate(1, selectedDate);
        }

        ArrayList<Integer> foodIds = null;
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
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                //Build date string with selected day
                dateString = (month + 1) + "/" + day + "/" + year;
                Log.d(TAG, "OnSelectedDayChange: MMMM d, yyyy: "+ dateString);

                // Build Date object with selected day
                selectedDate.set(year, month, day);

                try{
                    updateMealPlanDisplay(view, selectedDate.getTime());
                }catch (JSONException e){
                    e.printStackTrace();
                    selectedDayMenuTextView.setText("Catastrophic database error");
                }

                //TODO High Priority Feature
                // Send a request to MenuDAO for the menu for selectedDate and selectedPet
                // selectedDate is currently java.util.Calendar, interacting with the database
                // may be easier with java.sql.Date, either start with it from the beginning or
                // convert.
                // https://docs.oracle.com/javase/7/docs/api/java/sql/Date.html

            }
        });
    }
}
