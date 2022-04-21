package com.example.myapplication;

import java.util.ArrayList;
import java.util.Date;

public class DailyMealPlanEngine {

    public enum FoodType{LEAFYGREEN, VEGETABLE, PROTEIN}
    final int MS_PER_SECOND = 1000;
    final int SECONDS_PER_MINUTE = 60;
    final int MINUTES_PER_HOUR = 60;
    final int HOURS_PER_DAY = 24;

    class Lizard{
        private int id;
        private String name;
        private Date dateOfBirth;
        private int sizeCentimeters;

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
        public FoodType getFoodType(){return this.foodType;}
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

    private static DailyMealPlanEngine DMPEngine = null;

    private DailyMealPlanEngine() {
        //Populate lists
        lizardList.add(new Lizard(0, "Shenron", 90, new Date(2014, 3, 26)));
        lizardList.add(new Lizard(1, "Godzilla", 60, new Date(2022, 4, 20)));
        lizardList.add(new Lizard(2, "Dragonite", 40, new Date(2021, 12, 25)));

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
        long ageMs = new Date().getTime() - pet.dateOfBirth.getTime();
        int ageDays = (int) (ageMs / (MS_PER_SECOND * SECONDS_PER_MINUTE * MINUTES_PER_HOUR * HOURS_PER_DAY));
        int ageMonths = ageDays % 31;

        if(ageMonths < 1){
            //80% Proteins
            //10% LeafyGreens
            //10% Vegetables
            //MANDATORY PINHEAD CRICKETS EVERY DAY
        }else if(ageMonths < 3){
            //65% Proteins
            //25% Leafy Greens
            //10% Vegetables
            //MANDATORY CRICKETS EVERY DAY
        }else if(ageMonths < 18){
            //50% Proteins
            //25% Leafy Greens
            //10% Vegetables
            //MANDATORY CRICKETS EVERY DAY
        }else{
            //25% Proteins
            //55% Leafy Greens
            //20% Vegetables
        }

        //Create menu object for this pet today
        //for(food:foods)
        //      if available
        //          if !cooldown && food type not handled
        //              foodIdList.add(food)
        //              food type handled
        //          cooldown--
    }

    public void generateMealPlanForPetFuture(int requestedPetId){

        //oh this might be complicated.

        //TODO Question : Are multiple pets fed all together or separately?
        // Dumb question. They have to be separate. You can't feed the same meal plan to lizards in
        // different age brackets

        //If they're fed all together, cooldown field can stay attached to the respective food

        //If they're fed separately, each pet will need a different cooldown timer for every food
        //If this is  the case, store the cooldown attached to the pet, not to the food

        //Read the future: Mod the max cooldown of a food with the distance in days from today and
        // the target date to decide if that food can be used on that date.
    }
}
