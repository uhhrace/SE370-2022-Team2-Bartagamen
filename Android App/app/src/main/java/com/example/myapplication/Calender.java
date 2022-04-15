package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Calendar;

public class Calender extends BartScreen {

    CalendarView calendarView;
    TextView selectedDayMenuTextView;
    static final String TAG = "CalActivity";
    public String dateString;
    Calendar selectedDate;

    public Calender(){
        selectedDate = Calendar.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.calender, container, false);

        setCalendarViewListener(view);

        return view;
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

                // Update TextView notifying the user they selected the correct Date
                selectedDayMenuTextView.setText("Menu for " + dateString + ":");

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
