package com.example.myapplication;

import android.content.Intent;
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

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class Pets extends BartScreen {

    // Avoid hard-coded values, declare here
    final int DAYS_PER_WEEK = 7;

    ToggleButton sunButton;
    ToggleButton monButton;
    ToggleButton tueButton;
    ToggleButton wedButton;
    ToggleButton thuButton;
    ToggleButton friButton;
    ToggleButton satButton;
    ToggleButton[] dayButtons = new ToggleButton[DAYS_PER_WEEK];
    AppCompatButton monthButton;
    int currentDay;

    TextView dateView;
    Date d = new Date();
    CharSequence s  = DateFormat.format("MMMM d, yyyy ", d.getTime());
    int help;



    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pets, container, false);

        dateView = view.findViewById(R.id.dateField);
        dateView.setText("Date: "+s);
        if(d.getDay()==0){
            help = 0;
        }else if(d.getDay()==1){
            help = 1;}
        else if(d.getDay()==2){
            help = 2;}
        else if(d.getDay()==3){
            help = 3;}
        else if(d.getDay()==4){
            help = 4;}
        else if(d.getDay()==5){
            help = 5;}else {help = 6;}


        setDayButtonListeners(view);

        setMonthButtonListener(view);

        autoSelectDayOfWeek();

        //TODO low priority
        // check button sizes, if < 100dp, change SUN -> S, MON -> M, etc

        return view;
    }

    void autoSelectDayOfWeek(){
        // check current day of the week, highlight current day
        //Calendar calendar = Calendar.getInstance();

        //The Calendar.DAY_OF_WEEK enum starts with Mon at position 1, ends with Sun at position 7
        currentDay = d.getDay();
        //Decrement the offset to match with our array
        dayButtons[currentDay].performClick();
    }

    void setMonthButtonListener(View view){

        // Assign buttons
        monthButton = (AppCompatButton) view.findViewById(R.id.ViewMonthlyButton);

        monthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeScreenToCalendar();
            }
        });
    }

    void setDayButtonListeners(View view){
        // Assign buttons
        sunButton = (ToggleButton) view.findViewById(R.id.sunButton);
        monButton = (ToggleButton) view.findViewById(R.id.monButton);
        tueButton = (ToggleButton) view.findViewById(R.id.tueButton);
        wedButton = (ToggleButton) view.findViewById(R.id.wedButton);
        thuButton = (ToggleButton) view.findViewById(R.id.thuButton);
        friButton = (ToggleButton) view.findViewById(R.id.friButton);
        satButton = (ToggleButton) view.findViewById(R.id.satButton);

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
                public void onClick(View view) {

                    // Reset every other button to unpressed colors
                    for(ToggleButton butt : dayButtons){
                        butt.setBackground(getResources().getDrawable(R.drawable.rounded_corner_inactive));
                        butt.setTextColor( getResources().getColor(R.color.buttonDarkBlue));

                    }
                    // Set clicked button to pressed colors
                    button.setBackground(getResources().getDrawable(R.drawable.rounded_corner_active));
                    button.setTextColor(getResources().getColor(R.color.white));


                    String buttonText = (String) button.getText();
                    int helpCal;
                    if (buttonText.equals("SUN")){
                        helpCal = 0;
                    }else if (buttonText.equals("MON")){
                        helpCal = 1;
                    }else if (buttonText.equals("TUE")){
                        helpCal = 2;
                    }else if (buttonText.equals("WED")){
                        helpCal = 3;
                    }else if (buttonText.equals("THU")){
                        helpCal = 4;
                    }else if (buttonText.equals("FRI")){
                        helpCal = 5;}else{helpCal = 6;}


                    int dateDiff = currentDay - helpCal;

                    Instant now = Instant.now();
                    // Difference between today and selected day in seconds
                    int secondsDiff = dateDiff * 86400;
                    Instant then = now.plusSeconds(-secondsDiff);

                    Date selectedDay = Date.from(then);
                    CharSequence selectedDate  = DateFormat.format("MMMM d, yyyy ", selectedDay.getTime());
                    dateView.setText(""+selectedDate);




                }
            });
        }
        }
}
