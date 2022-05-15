package com.example.myapplication;

import java.util.ArrayList;
import java.util.Date;

class MealPlan{
    private Date date;
    private int petId;
    private ArrayList<Integer> foodIdList;

    MealPlan(){
        this.date = null;
        this.petId = -1;
        this.foodIdList = new ArrayList<>();
    }

    MealPlan(int petId, Date date, ArrayList<Integer> foodIdList){
        this.petId = petId;
        this.date = date;
        this.foodIdList = foodIdList;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public Date getDate(){
        return this.date;
    }
    public void setPetId(int petId){
        this.petId = petId;
    }
    public int getPetId(){
        return petId;
    }

    public void addFoodId(int foodId){
        this.foodIdList.add(foodId);
    }

    public ArrayList<Integer> getFoodIdList(){
        return foodIdList;
    }
}