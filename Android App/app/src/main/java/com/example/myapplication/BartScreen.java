package com.example.myapplication;

import androidx.fragment.app.Fragment;

public class BartScreen extends Fragment {

    public MainActivity main;

    protected void changeScreenToCalendar(){
        main.changeScreenToCalendar();
    }

    protected void changeScreenToFoodBank(){
        main.changeScreenToFoodBank();
    }

    public void attach(MainActivity foreignMain){
        main = foreignMain;
    }
}