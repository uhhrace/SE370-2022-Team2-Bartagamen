package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Date;

public class DailyMealPlanEngine extends AppCompatActivity {

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

    class MealPlan{
        private int id;
        private Date date;
        private int petId;
        private ArrayList<Integer> foodIdList;

        MealPlan(){
            this.id = -1;
            this.date = null;
            this.petId = -1;
            this.foodIdList = new ArrayList<>();
        }

        public int getId(){return id;}
        public Date getDate(){return date;}
        public int getPetId(){return petId;}
        public ArrayList<Integer> getFoodIdList(){return foodIdList;}
    }

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
        todaysMealPlan.petId = pet.getId();
        todaysMealPlan.date = new Date();
        pet.decrementCooldowns();
        ArrayList recentFoods = pet.getRecentFoods();
        ArrayList<FoodItem> availableFoods = dao.getAvailableFoods();

        if(ageMonths < 1){
            //80% Proteins
            //10% LeafyGreens
            //10% Vegetables
            //MANDATORY PINHEAD CRICKETS EVERY DAY
            todaysMealPlan.foodIdList.add(PINHEAD_CRICKETS_ID);
            foodTypeHandled[FoodType.PROTEIN.ordinal()] = true;
        }else if(ageMonths < 3){
            //65% Proteins
            //25% Leafy Greens
            //10% Vegetables
            //MANDATORY CRICKETS EVERY DAY
            todaysMealPlan.foodIdList.add(CRICKETS_ID);
            foodTypeHandled[FoodType.PROTEIN.ordinal()] = true;
        }else if(ageMonths < 18){
            //50% Proteins
            //35% Leafy Greens
            //15% Vegetables
            //MANDATORY CRICKETS EVERY DAY
            todaysMealPlan.foodIdList.add(CRICKETS_ID);
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
                    todaysMealPlan.foodIdList.add(food.getId());
                    pet.addRecentFood(food);
                    foodTypeHandled[food.getFoodType()] = true;
                }
            }
        }

        mealPlanList.add(todaysMealPlan);

        mealItems = mealItems+3;

        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        final PetScreenController  frg = new PetScreenController ();
        ft.replace(R.id.container, frg);

        final Bundle bdl = new Bundle();
        bdl.putString("count", ""+mealItems);
        frg.setArguments(bdl);

        return todaysMealPlan;
    }

    public void generateMealPlanForPetFuture(int requestedPetId){

        //oh this might be complicated.

        //Read the future: Create new instance of lizard, iterate 14 times, process cooldowns,
        //generate meal plan for today+n iterations. Mod the max cooldown of a food with the distance
        //in days from today and the target date to decide if that food can be used on that date.
    }
}
