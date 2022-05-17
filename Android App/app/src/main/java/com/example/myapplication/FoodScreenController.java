package com.example.myapplication;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FoodScreenController extends BartScreenController {

    DAO dao;
    DailyMealPlanEngine dmp;

    LayoutInflater viewInflater;
    View foodListContainer;

    protected void changeScreenToCalendar(){
        super.changeScreenToCalendar();
        dao.updateAvailableFoods();
    }

    protected void changeScreenToFoodBank(){
        super.changeScreenToFoodBank();
        dao.updateAvailableFoods();
    }

    protected void changeScreenToHome(){
        super.changeScreenToHome();
        dao.updateAvailableFoods();
    }

    protected void changeScreenToPets(){
        super.changeScreenToPets();
        dao.updateAvailableFoods();
    }

    protected void changeScreenToAddPet() {
        super.changeScreenToAddPet();
        dao.updateAvailableFoods();
    }

    protected void changeScreenToPets(String petName){
        super.changeScreenToPets(petName);
        dao.updateAvailableFoods();
    }

    public FoodScreenController(){
        dao = DAO.getDAO();
        dmp = DailyMealPlanEngine.getDMPEngine();
    }

    public void setFoodListContainer(LayoutInflater viewInflater, View foodListContainer) {
        this.foodListContainer = foodListContainer;
        this.viewInflater = viewInflater;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.food_bank_screen, container, false);

        setFoodListContainer(inflater, container);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        try{
            populateFoodListButtons(viewInflater, foodListContainer);
            checkAvailableFoods();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void checkAvailableFoods(){

        ArrayList<FoodItem> availableFoods = dao.getAvailableFoods();

        View mainView = ((ViewGroup)getView().getParent());
        FoodItem food = null;
        ToggleButton butt = null;

        for(int i = 0; i < availableFoods.size(); i++){
            food = availableFoods.get(i);

            butt = mainView.findViewWithTag(food.getId()).findViewById(R.id.foodBankButton);

            butt.setChecked((food.isAvailable()));

        }

    }

    private void populateFoodListButtons(LayoutInflater inflater, View view) throws JSONException {

        LinearLayout destinationDiv;

        SQLiteDatabase t = dao.getWritableDatabase();

        ArrayList<FoodItem> foodList = dao.getUserFoodList();

        for(int i = 0; i < foodList.size(); i++){
            View toAdd;
            ToggleButton button;
            TextView itemText;
            FoodItem food = foodList.get(i);
            DailyMealPlanEngine.FoodType foodType = food.getFoodTypeEnum();

            switch (foodType){
                case LEAFYGREEN:
                default:
                    destinationDiv = view.findViewById(R.id.TypeLeafyGreens);
                    break;
                case VEGETABLE:
                    destinationDiv = view.findViewById(R.id.TypeVegetables);
                    break;
                case PROTEIN:
                    destinationDiv = view.findViewById(R.id.TypeBugs);
                    break;
            }

            toAdd = inflater.inflate(R.layout.food_bank_item, destinationDiv, false);
            button = toAdd.findViewById(R.id.foodBankButton);
            itemText = toAdd.findViewById(R.id.text);
            itemText.setText(food.getName());
            toAdd.setTag(food.getId());

            button.setChecked(food.isAvailable());

            //We're setting checked/unchecked in the code, so we don't want a listener
            //  to activate every time that happens. this needs to be an onClicked listener,
            //  so we only trigger for user input button presses
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    button.setChecked(button.isChecked());
                    dao.changeFoodAvailability((int) food.getId(), button.isChecked());
                }
            });

            destinationDiv.addView(toAdd);
        }
    } // end populateFoodList
}
