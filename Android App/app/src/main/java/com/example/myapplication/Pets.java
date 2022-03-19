package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Pets extends Fragment {

    ToggleButton sunButton;
    ToggleButton monButton;
    ToggleButton tueButton;
    ToggleButton wedButton;
    ToggleButton thuButton;
    ToggleButton friButton;
    ToggleButton satButton;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pets, container, false);

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
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Reset every other button to unpressed colors
                    for(ToggleButton butt : dayButtons){
                        butt.setBackgroundColor( getResources().getColor(R.color.buttonLightBlue));
                        butt.setTextColor( getResources().getColor(R.color.buttonDarkBlue));
                    }
                    // Set clicked button to pressed colors
                    button.setBackgroundColor( getResources().getColor(R.color.buttonDarkBlue));
                    button.setTextColor(getResources().getColor(R.color.white));
                }
            });
        }

        //TODO check button sizes, if < 100dp, change SUN -> S, MON -> M, etc

        //TODO check current day of the week, highlight current day
        
        return view;
    }




}
