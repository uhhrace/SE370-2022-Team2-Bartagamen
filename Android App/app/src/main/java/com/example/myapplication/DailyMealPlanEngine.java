package com.example.myapplication;

import java.util.ArrayList;
import java.util.Date;

public class DailyMealPlanEngine {

    public enum FoodType{LEAFYGREEN, VEGETABLE, PROTEIN}
    final int MS_PER_SECOND = 1000;
    final int SECONDS_PER_MINUTE = 60;
    final int MINUTES_PER_HOUR = 60;
    final int HOURS_PER_DAY = 24;
    final int INDEX_NOT_FOUND = -1;
    final int PINHEAD_CRICKETS_ID = 15;
    final int CRICKETS_ID = 10;

    class Lizard{
        private int id;
        private String name;
        private Date dateOfBirth;
        private int sizeCentimeters;

        private ArrayList<FoodItem> recentFoods;

        Lizard(int id, String newName, int sizeCentimeters, Date dateOfBirth){
            this.id = id;
            this.name = newName;
            this.dateOfBirth = dateOfBirth;
            this.sizeCentimeters = sizeCentimeters;
        }

        public int getId(){return this.id;}
        public String getName(){return this.name;}
        public Date getDateOfBirth(){return this.dateOfBirth;}
        public int getSizeCentimeters(){return this.sizeCentimeters;}

        public void addRecentFood(FoodItem recentFood){
            recentFoods.add(recentFood);
        }

        // Decrement cooldowns for all recent foods
        public void decrementCooldowns(){
            for(FoodItem food : recentFoods){
                if(food.getCoolDown() == 0){
                    recentFoods.remove(food);
                }else{
                    food.decrementCooldown();
                }
            }
        }

    }

    class FoodItem{
        private int id;
        private String name;
        private int coolDown;
        private FoodType foodType;
        private boolean available;

        FoodItem(int id, FoodType type, String name){
            this.id = id;
            this.name = name;
            this.coolDown = 0;
            this.foodType = type;
            this.available = false;
        }

        public int getId(){return this.id;}
        public String getName(){return this.name;}
        public int getCoolDown(){return this.coolDown;}
        public void decrementCooldown(){coolDown--;}
        public int getFoodType(){return this.foodType.ordinal();}
        public boolean isAvailable(){return this.available;}
    }

    class MealPlan{
        private int id;
        private Date date;
        private int petId;
        private ArrayList<Integer> foodIdList;
    }

    ArrayList<Lizard> lizardList;
    ArrayList<FoodItem> foodList;
    ArrayList<MealPlan> mealPlanList;

    public ArrayList getFoodList(){
        return foodList;
    }

    private static DailyMealPlanEngine DMPEngine = null;

    private DailyMealPlanEngine() {
        //Populate lists
        lizardList.add(new Lizard(0, "Shenron", 90, new Date(2014, 3, 26)));
        lizardList.add(new Lizard(1, "Godzilla", 60, new Date(2022, 4, 20)));
        lizardList.add(new Lizard(2, "Dragonite", 40, new Date(2021, 12, 25)));


        //TODO High Priority
        // Build food list from JSON object
        foodList.add(new FoodItem(0, FoodType.LEAFYGREEN, "Collard Greens"));
        foodList.add(new FoodItem(1, FoodType.LEAFYGREEN, "Mustard Greens"));
        foodList.add(new FoodItem(2, FoodType.LEAFYGREEN, "Romaine"));
        foodList.add(new FoodItem(3, FoodType.LEAFYGREEN, "Dandelions"));
        foodList.add(new FoodItem(4, FoodType.LEAFYGREEN, "Turnip Greens"));

        foodList.add(new FoodItem(5, FoodType.VEGETABLE, "Squash"));
        foodList.add(new FoodItem(6, FoodType.VEGETABLE, "Zucchini"));
        foodList.add(new FoodItem(7, FoodType.VEGETABLE, "Sweet Potato"));
        foodList.add(new FoodItem(8, FoodType.VEGETABLE, "Broccoli"));
        foodList.add(new FoodItem(9, FoodType.VEGETABLE, "Peas"));

        foodList.add(new FoodItem(10, FoodType.PROTEIN, "Crickets"));
        foodList.add(new FoodItem(11, FoodType.PROTEIN, "Mealworms"));
        foodList.add(new FoodItem(12, FoodType.PROTEIN, "Grasshoppers"));
        foodList.add(new FoodItem(13, FoodType.PROTEIN, "Earthworms"));
        foodList.add(new FoodItem(14, FoodType.PROTEIN, "Calciworms"));
        foodList.add(new FoodItem(15, FoodType.PROTEIN, "Pinhead Crickets"));
    }

    public static DailyMealPlanEngine getDMPEngine(){
        if(DMPEngine == null){
            DMPEngine = new DailyMealPlanEngine();
        }
        return DMPEngine;
    }

    public void generateMealPlanForPetToday(int requestedPetId){

        //Currently petId is also the index.
        //TODO Figure out how to find the index of the pet when this is not the case
        Lizard pet = lizardList.get(requestedPetId);

        // Get Age in Months
        long ageMs = new Date().getTime() - pet.getDateOfBirth().getTime();
        int ageDays = (int) (ageMs / (MS_PER_SECOND * SECONDS_PER_MINUTE * MINUTES_PER_HOUR * HOURS_PER_DAY));
        int ageMonths = ageDays % 31;

        boolean[] foodTypeHandled = {false, false, false};

        //Create menu object for this pet today
        MealPlan todaysMealPlan = new MealPlan();
        todaysMealPlan.petId = pet.getId();
        todaysMealPlan.date = new Date();
        pet.decrementCooldowns();

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

        // for each food in foods
        for( FoodItem food : foodList ){
            if(food.available){
                // If food type not yet currently in meal plan
                if (!foodTypeHandled[food.getFoodType()]){
                    // If food not in recent foods
                    if(INDEX_NOT_FOUND == pet.recentFoods.indexOf(food.getId())){
                        todaysMealPlan.foodIdList.add(food.getId());
                        pet.addRecentFood(food);
                        foodTypeHandled[food.getFoodType()] = true;
                    }
                }
            }
        }

        mealPlanList.add(todaysMealPlan);
    }

    public void generateMealPlanForPetFuture(int requestedPetId){

        //oh this might be complicated.

        //Read the future: Mod the max cooldown of a food with the distance in days from today and
        // the target date to decide if that food can be used on that date.
    }
}
