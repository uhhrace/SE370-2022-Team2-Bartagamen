package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Date;

public class DailyMealPlanEngine extends AppCompatActivity {

    public enum FoodType{LEAFYGREEN, VEGETABLE, PROTEIN}
    final int MS_PER_SECOND = 1000;
    final int SECONDS_PER_MINUTE = 60;
    final int MINUTES_PER_HOUR = 60;
    final int HOURS_PER_DAY = 24;
    final int DAYS_PER_MONTH = 31;
    final int PINHEAD_CRICKETS_ID = 30;
    final int CRICKETS_ID = 26;
    final int FOOD_VARIETY_DAY_DIFF = 3;

    DAO dao;

    private static DailyMealPlanEngine DMPEngine = null;

    private DailyMealPlanEngine() {
        dao = DAO.getDAO();
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

        //This takes time to process
        ArrayList<FoodItem> userFoods = dao.getUserFoodList();

        FoodItem food;

        if(ageMonths < 1){
            //80% Proteins
            //10% LeafyGreens
            //10% Vegetables
            //MANDATORY PINHEAD CRICKETS EVERY DAY
            datesMealPlan.addFoodId(PINHEAD_CRICKETS_ID);
            dao.addRecentFood(PINHEAD_CRICKETS_ID, pet.getId(), requestedDate);
            foodTypeHandled[FoodType.PROTEIN.ordinal()] = true;
        }else if(ageMonths < 3){
            //65% Proteins
            //25% Leafy Greens
            //10% Vegetables
            //MANDATORY CRICKETS EVERY DAY
            datesMealPlan.addFoodId(CRICKETS_ID);
            dao.addRecentFood(CRICKETS_ID, pet.getId(), requestedDate);
            foodTypeHandled[FoodType.PROTEIN.ordinal()] = true;
        }else if(ageMonths < 18){
            //50% Proteins
            //35% Leafy Greens
            //15% Vegetables
            //MANDATORY CRICKETS EVERY DAY
            datesMealPlan.addFoodId(CRICKETS_ID);
            dao.addRecentFood(CRICKETS_ID, pet.getId(), requestedDate);
            foodTypeHandled[FoodType.PROTEIN.ordinal()] = true;
        }else{
            //25% Proteins
            //55% Leafy Greens
            //20% Vegetables
        }

        Date dateLastAte;

        // for each available food
        for(int i = 0; i < userFoods.size(); i++){

            food = userFoods.get(i);

            // If food type not yet currently in meal plan
            if (!foodTypeHandled[userFoods.get(i).getFoodType()]){

                // And food is available
                if(food.isAvailable()){

                    // If food not recent
                    dateLastAte = dao.getLastAte(food.getId(), pet.getId());

                    long msDiff = requestedDate.getTime() - dateLastAte.getTime();
                    long dayDiff = msDiff / (MS_PER_SECOND * SECONDS_PER_MINUTE * MINUTES_PER_HOUR * HOURS_PER_DAY);

                    //If dateLastAte after today, its okay to eat today
                    if(dateLastAte.after(requestedDate) || dayDiff > FOOD_VARIETY_DAY_DIFF){
                        datesMealPlan.addFoodId(food.getId());
                        dao.addRecentFood(food.getId(), pet.getId(), requestedDate);
                        foodTypeHandled[food.getFoodType()] = true;
                    }

                }
            }
        }

        dao.addMealPlan(datesMealPlan);

        return datesMealPlan;
    }

    public void generateMealPlanForPetFuture(int requestedPetId){

        //oh this might be complicated.

        //Read the future: Create new instance of lizard, iterate 14 times, process cooldowns,
        //generate meal plan for today+n iterations. Mod the max cooldown of a food with the distance
        //in days from today and the target date to decide if that food can be used on that date.

        Lizard lizard = dao.getLizard(requestedPetId);

    }
}
