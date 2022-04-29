package com.example.myapplication;

public class FoodItem {
    private int id;
    private String name;
    private int coolDown;
    private DailyMealPlanEngine.FoodType foodType;
    private boolean available;

    FoodItem(int id, DailyMealPlanEngine.FoodType type, String name){
        this.id = id;
        this.name = name;
        this.coolDown = 0;
        this.foodType = type;
        this.available = false;
    }

    FoodItem(int id, DailyMealPlanEngine.FoodType type, String name, boolean available){
        this.id = id;
        this.name = name;
        this.coolDown = 0;
        this.foodType = type;
        this.available = available;
    }

    public int getId(){return this.id;}
    public String getName(){return this.name;}
    public int getCoolDown(){return this.coolDown;}
    public void decrementCooldown(){coolDown--;}
    public int getFoodType(){return this.foodType.ordinal();}
    public boolean isAvailable(){return this.available;}
}
