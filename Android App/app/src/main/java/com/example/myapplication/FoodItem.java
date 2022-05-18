package com.example.myapplication;

public class FoodItem {
    private int id;
    private String name;
    private DailyMealPlanEngine.FoodType foodType;
    private boolean available;

    FoodItem(int id, DailyMealPlanEngine.FoodType type, String name){
        this.id = id;
        this.name = name;
        this.foodType = type;
        this.available = false;
    }

    FoodItem(int id, DailyMealPlanEngine.FoodType type, String name, boolean available){
        this.id = id;
        this.name = name;
        this.foodType = type;
        this.available = available;
    }

    public int getId(){return this.id;}
    public String getName(){return this.name;}
    public DailyMealPlanEngine.FoodType getFoodTypeEnum(){return this.foodType;}
    public int getFoodType(){return this.foodType.ordinal();}
    public boolean isAvailable(){return this.available;}
}
