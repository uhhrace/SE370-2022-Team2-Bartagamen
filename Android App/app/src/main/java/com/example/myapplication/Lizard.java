package com.example.myapplication;

import java.util.ArrayList;
import java.util.Date;

public class Lizard{
    private int id;
    private String name;
    private Date dateOfBirth;
    private String sizeCentimeters;

    private ArrayList<FoodItem> recentFoods;

    Lizard(int id, String newName, String sizeCentimeters, Date dateOfBirth){
        this.id = id;
        this.name = newName;
        this.dateOfBirth = dateOfBirth;
        this.sizeCentimeters = sizeCentimeters;
        this.recentFoods = new ArrayList<>();
    }

    public int getId(){return this.id;}
    public String getName(){return this.name;}
    public Date getDateOfBirth(){return this.dateOfBirth;}
    public String getSizeCentimeters(){return this.sizeCentimeters;}

    public void addRecentFood(FoodItem recentFood){
        recentFoods.add(recentFood);
    }
    public ArrayList getRecentFoods(){ return recentFoods; }

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
