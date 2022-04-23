package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FoodScreenController extends BartScreenController {

    DAO dbFood1;
    boolean executed = false;

//    ToggleButton toggleCollardGreens, toggleMustardGreens, toggleRomaine, toggleDandelion, toggleTurnipGreens, toggleBokChoy,
//            toggleChicory, toggleEscarole, toggleWIldPlants, toggleCilantro, toggleWatercress, toggleGrapeLeaves, toggleSquash,
//            toggleZucchini, toggleBroccoli, togglePeas, toggleCarrot, toggleBeans, toggleOkra, toggleBeanSprouts, toggleTofu,
//            toggleBellPepper, toggleEndive, toggleCrickets, toggleMealworms, toggleGrasshoppers, toggleEarthworms, toggleCalciWorms;
//
//    TextView textCollardGreens, textMustardGreens, textRomaine, textDandelion, textTurnipGreens, textBokChoy,
//            textChicory, textEscarole, textWIldPlants, textCilantro, textWatercress, textGrapeLeaves, textSquash,
//            textZucchini, textBroccoli, textPeas, textCarrot, textBeans, textOkra, textBeanSprouts, textTofu,
//            textBellPepper, textEndive, textCrickets, textMealworms, textGrasshoppers, textEarthworms, textCalciWorms;

    //Replace these with arrays built from DB queries
    private String[] greensNames =  { "Collard Greens", "Mustard Greens", "Romaine", "Dandelion", "Turnip Greens" };
    private String[] vegNames = {"Squash", "Zucchini", "Sweet Potato", "Broccoli", "Peas"};
    private String[] bugNames = {"Crickets", "Mealworms", "Grasshoppers", "Earthworms", "Calciworms"};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.food_bank_screen, container, false);
        try{
            dbFood1 = DAO.getDAO();
        }catch (Exception e){

        }

        try{
            populateFoodList(inflater, view);
        }catch (JSONException e){
            e.printStackTrace();
        }

        view.findViewById(R.id.crickets);

        return view;
    }

    void populateFoodList(LayoutInflater inflater, View view) throws JSONException {
        LinearLayout greensDiv = view.findViewById(R.id.TypeLeafyGreens);
        LinearLayout vegDiv = view.findViewById(R.id.TypeVegetables);
        LinearLayout bugDiv = view.findViewById(R.id.TypeBugs);

        LinearLayout destinationDiv = null;

        // TODO High Priority
        //  Once DB is implemented, build arrays from DB queries
        //  greensNames = SELECT NAME FROM FOOD WHERE TYPE = LEAFYGREEN

        DAO dao = DAO.getDAO();
        JSONArray foodList = dao.getFoodList();

        for(int i = 0; i < foodList.length(); i++){
            View toAdd;
            ToggleButton button;
            TextView itemText;
            JSONObject food = foodList.getJSONObject(i);
            switch (food.get("type").toString()){
                case "LEAFYGREEN":
                    destinationDiv = view.findViewById(R.id.TypeLeafyGreens);
                    break;
                case "VEGETABLE":
                    destinationDiv = view.findViewById(R.id.TypeVegetables);
                    break;
                case "PROTEIN":
                    destinationDiv = view.findViewById(R.id.TypeBugs);
                    break;
                default:
                    destinationDiv = view.findViewById(R.id.TypeLeafyGreens);
                    break;
            }

            toAdd = inflater.inflate(R.layout.food_bank_item, destinationDiv, false);
            button = toAdd.findViewById(R.id.foodBankButton);
            itemText = toAdd.findViewById(R.id.text);
            itemText.setText(food.get("name").toString());

            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        dbFood1.changeFoodAvailability((int) food.get("id"), isChecked);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            destinationDiv.addView(toAdd);
        }
    } // end populateFoodList
}
