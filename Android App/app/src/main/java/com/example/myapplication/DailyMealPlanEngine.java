package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DailyMealPlanEngine extends AppCompatActivity {

    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

    public enum FoodType{LEAFYGREEN, VEGETABLE, PROTEIN}
    final int MS_PER_SECOND = 1000;
    final int SECONDS_PER_MINUTE = 60;
    final int MINUTES_PER_HOUR = 60;
    final int HOURS_PER_DAY = 24;
    final int DAYS_PER_MONTH = 31;
    final int INDEX_NOT_FOUND = -1;
    final int PINHEAD_CRICKETS_ID = 30;
    final int CRICKETS_ID = 26;

    DAO dao;

    ArrayList<MealPlan> mealPlanList = new ArrayList<>();

    private static DailyMealPlanEngine DMPEngine = null;

    private DailyMealPlanEngine() {

        dao = DAO.getDAO();
//        dao.addPet( 1,"Shenron", "90", "2014/3/26");
//        dao.addPet(2,"Godzilla", "60", "2022/4/20");
//        dao.addPet( 3,"Dragonite", "40", "2021/12/25");
    }

    public static DailyMealPlanEngine getDMPEngine(){
        if(DMPEngine == null){
            DMPEngine = new DailyMealPlanEngine();
        }
        return DMPEngine;
    }

    public MealPlan generateMealPlanForPetOnDate(int requestedPetId, Date requestedDate){
        Lizard pet = dao.getLizard(requestedPetId);

        long ageMs = requestedDate.getTime() - pet.getDateOfBirth().getTime();
        int ageDays = (int) (ageMs / (MS_PER_SECOND * SECONDS_PER_MINUTE * MINUTES_PER_HOUR * HOURS_PER_DAY));
        int ageMonths = (int) Math.floor(ageDays / DAYS_PER_MONTH);

        boolean[] foodTypeHandled = {false, false, false};

        MealPlan datesMealPlan = new MealPlan();
        datesMealPlan.setPetId(requestedPetId);
        datesMealPlan.setDate(new Date(requestedDate.getYear(), requestedDate.getMonth(), requestedDate.getDate()));

        ArrayList recentFoods = pet.getRecentFoods();
        ArrayList<FoodItem> availableFoods = dao.getAvailableFoods();

        FoodItem food;

        if(ageMonths < 1){
            //80% Proteins
            //10% LeafyGreens
            //10% Vegetables
            //MANDATORY PINHEAD CRICKETS EVERY DAY
            datesMealPlan.addFoodId(PINHEAD_CRICKETS_ID);
            foodTypeHandled[FoodType.PROTEIN.ordinal()] = true;
        }else if(ageMonths < 3){
            //65% Proteins
            //25% Leafy Greens
            //10% Vegetables
            //MANDATORY CRICKETS EVERY DAY
            datesMealPlan.addFoodId(CRICKETS_ID);
            foodTypeHandled[FoodType.PROTEIN.ordinal()] = true;
        }else if(ageMonths < 18){
            //50% Proteins
            //35% Leafy Greens
            //15% Vegetables
            //MANDATORY CRICKETS EVERY DAY
            datesMealPlan.addFoodId(CRICKETS_ID);
            foodTypeHandled[FoodType.PROTEIN.ordinal()] = true;
        }else{
            //25% Proteins
            //55% Leafy Greens
            //20% Vegetables
        }

        // for each available food
        for(int i = 0; i < availableFoods.size(); i++){

            food = availableFoods.get(i);

            // TODO date instead of decrement

            // If food type not yet currently in meal plan
            if (!foodTypeHandled[availableFoods.get(i).getFoodType()]){
                // If food not in recent foods
                if(!recentFoods.contains(food.getId())){
                    datesMealPlan.addFoodId(food.getId());
                    pet.addRecentFood(food);
                    foodTypeHandled[food.getFoodType()] = true;
                }
            }
        }

        mealPlanList.add(datesMealPlan);
        dao.addMealPlan(datesMealPlan);

        return datesMealPlan;
    }

    public MealPlan generateMealPlanForPetToday(int requestedPetId){

        //count mealItems
        int mealItems = 0;

        //Currently petId is also the index.
        //TODO Figure out how to find the index of the pet when this is not the case
        Lizard pet = dao.getLizard(requestedPetId);

        // Get Age in Months
        long ageMs = new Date().getTime() - pet.getDateOfBirth().getTime();
        int ageDays = (int) (ageMs / (MS_PER_SECOND * SECONDS_PER_MINUTE * MINUTES_PER_HOUR * HOURS_PER_DAY));
        int ageMonths = (int) Math.floor(ageDays / DAYS_PER_MONTH);

        boolean[] foodTypeHandled = {false, false, false};

        //Create menu object for this pet today
        MealPlan todaysMealPlan = new MealPlan();
        todaysMealPlan.setPetId(pet.getId());
        Date today = new Date();

        todaysMealPlan.setDate(new Date(today.getYear(), today.getMonth(), today.getDate()));

        pet.decrementCooldowns();
        ArrayList recentFoods = pet.getRecentFoods();
        ArrayList<FoodItem> availableFoods = dao.getAvailableFoods();

        if(ageMonths < 1){
            //80% Proteins
            //10% LeafyGreens
            //10% Vegetables
            //MANDATORY PINHEAD CRICKETS EVERY DAY
            todaysMealPlan.addFoodId(PINHEAD_CRICKETS_ID);
            foodTypeHandled[FoodType.PROTEIN.ordinal()] = true;
        }else if(ageMonths < 3){
            //65% Proteins
            //25% Leafy Greens
            //10% Vegetables
            //MANDATORY CRICKETS EVERY DAY
            todaysMealPlan.addFoodId(CRICKETS_ID);
            foodTypeHandled[FoodType.PROTEIN.ordinal()] = true;
        }else if(ageMonths < 18){
            //50% Proteins
            //35% Leafy Greens
            //15% Vegetables
            //MANDATORY CRICKETS EVERY DAY
            todaysMealPlan.addFoodId(CRICKETS_ID);
            foodTypeHandled[FoodType.PROTEIN.ordinal()] = true;
        }else{
            //25% Proteins
            //55% Leafy Greens
            //20% Vegetables
        }

        FoodItem food;

        // for each available food
        for(int i = 0; i < availableFoods.size(); i++){

            food = availableFoods.get(i);

            // TODO date instead of decrement

            // If food type not yet currently in meal plan
            if (!foodTypeHandled[availableFoods.get(i).getFoodType()]){
                // If food not in recent foods
                if(!recentFoods.contains(food.getId())){
                    todaysMealPlan.addFoodId(food.getId());
                    pet.addRecentFood(food);
                    foodTypeHandled[food.getFoodType()] = true;
                }
            }
        }

        mealPlanList.add(todaysMealPlan);
        dao.addMealPlan(todaysMealPlan);

        mealItems = mealItems+3;


        //push data from Activity to Fragment
        PetScreenController frg = new PetScreenController ();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        final Bundle bdl = new Bundle();
        bdl.putString("count", ""+mealItems);
        frg.setArguments(bdl);

        //This line is not working, idk why
      //  ft.replace(R.id.fragmentContainer, frg).commit();


        return todaysMealPlan;
    }

    public void generateMealPlanForPetFuture(int requestedPetId){

        //oh this might be complicated.

        //Read the future: Create new instance of lizard, iterate 14 times, process cooldowns,
        //generate meal plan for today+n iterations. Mod the max cooldown of a food with the distance
        //in days from today and the target date to decide if that food can be used on that date.
    }
}
