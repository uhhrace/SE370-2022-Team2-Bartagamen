package com.example.myapplication;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

public class Pets extends BartScreen {

    ToggleButton sunButton;
    ToggleButton monButton;
    ToggleButton tueButton;
    ToggleButton wedButton;
    ToggleButton thuButton;
    ToggleButton friButton;
    ToggleButton satButton;
    AppCompatButton monthButton;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pets, container, false);

        setDayButtonListeners(view);

        setMonthButtonListener(view);

        //TODO low priority
        // check button sizes, if < 100dp, change SUN -> S, MON -> M, etc

        //TODO high priority feature
        // check current day of the week, highlight current day
        
        return view;
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
        ToggleButton[] dayButtons = new ToggleButton[7];
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
                }
            });
        }
    }
}
