package com.example.myapplication;

import androidx.fragment.app.Fragment;

public class BartScreenController extends Fragment {

    public MainActivity main;

    protected void changeScreenToCalendar(){
        main.changeScreenToCalendar();
    }

    protected void changeScreenToFoodBank(){
        main.changeScreenToFoodBank();
    }

    protected void changeScreenToHome(){
        main.changeScreenToHome();
    }

    protected void changeScreenToPets(){
        main.changeScreenToPets();
    }

    //TODO Bryce
    // Add changeScreenToAddPet() calls main.changeScreenToAddPet

    protected void changeScreenToPets(String petName){ main.changeScreenToPets(petName); }

    public void attach(MainActivity foreignMain){
        main = foreignMain;
    }
}