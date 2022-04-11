package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

public class Home extends BartScreen {

    AppCompatButton petListButton;
    AppCompatButton foodBankButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);

        setButtonListeners(view);

        return view;
    }

    void setButtonListeners(View view){
        petListButton = view.findViewById(R.id.petListButton);
        foodBankButton = view.findViewById(R.id.foodBankButton);

        petListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // TODO high priority feature
                // Call func to expand pet list here
            }
        });

        foodBankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeScreenToFoodBank();
            }
        });
    }
}
