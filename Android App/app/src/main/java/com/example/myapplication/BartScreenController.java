package com.example.myapplication;

import androidx.fragment.app.Fragment;

public class BartScreenController extends Fragment {

    public MainActivity main;

    protected void changeScreenToCalendar(){
        main.changeScreenToCalendar();
    }

    protected void changeScreenToFoodBank(){
        main.changeScreenToFoodBank();
        main.foodScreenController.checkAvailableFoods();
    }

    protected void changeScreenToHome(){
        main.changeScreenToHome();
    }

    protected void changeScreenToPets(){
        main.changeScreenToPets();
    }

    protected void changeScreenToAddPet() {main.changeScreenToAddPet();}

    protected void changeScreenToPets(String petName){ main.changeScreenToPets(petName); }

    public void attach(MainActivity foreignMain){
        main = foreignMain;
    }
}