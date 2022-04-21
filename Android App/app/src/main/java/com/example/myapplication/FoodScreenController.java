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

public class FoodScreenController extends BartScreenController {

    FoodDAO dbFood;
    boolean executed = false;

    ToggleButton toggleCollardGreens, toggleMustardGreens, toggleRomaine, toggleDandelion, toggleTurnipGreens, toggleBokChoy,
            toggleChicory, toggleEscarole, toggleWIldPlants, toggleCilantro, toggleWatercress, toggleGrapeLeaves, toggleSquash,
            toggleZucchini, toggleBroccoli, togglePeas, toggleCarrot, toggleBeans, toggleOkra, toggleBeanSprouts, toggleTofu,
            toggleBellPepper, toggleEndive, toggleCrickets, toggleMealworms, toggleGrasshoppers, toggleEarthworms, toggleCalciWorms;

    TextView textCollardGreens, textMustardGreens, textRomaine, textDandelion, textTurnipGreens, textBokChoy,
            textChicory, textEscarole, textWIldPlants, textCilantro, textWatercress, textGrapeLeaves, textSquash,
            textZucchini, textBroccoli, textPeas, textCarrot, textBeans, textOkra, textBeanSprouts, textTofu,
            textBellPepper, textEndive, textCrickets, textMealworms, textGrasshoppers, textEarthworms, textCalciWorms;

    //Replace these with arrays built from DB queries
    private String[] greensNames =  { "Collard Greens", "Mustard Greens", "Romaine", "Dandelion", "Turnip Greens" };
    private String[] vegNames = {"Squash", "Zucchini", "Sweet Potato", "Broccoli", "Peas"};
    private String[] bugNames = {"Crickets", "Mealworms", "Grasshoppers", "Earthworms", "Calciworms"};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.food_bank_screen, container, false);
        dbFood = new FoodDAO(getContext());
        checkExecution();







//        toggleCollardGreens = view.findViewById(R.id.toggleColGreens);
//        toggleMustardGreens = view.findViewById(R.id.toggleMustardGreens);
//        toggleRomaine = view.findViewById(R.id.toggleRomaine);
//        toggleDandelion = view.findViewById(R.id.toggleDandelion);
//        toggleTurnipGreens = view.findViewById(R.id.toggleTurnipGreens);
//        toggleBokChoy = view.findViewById(R.id.toggleBokChoy);
//        toggleChicory = view.findViewById(R.id.toggleChicory);
//        toggleEscarole = view.findViewById(R.id.toggleEscarole);
//        toggleWIldPlants = view.findViewById(R.id.toggleWildPlants);
//        toggleCilantro = view.findViewById(R.id.toggleCilantro);
//        toggleWatercress = view.findViewById(R.id.toggleWatercress);
//        toggleGrapeLeaves = view.findViewById(R.id.toggleGrapeLeaves);
//        toggleSquash = view.findViewById(R.id.toggleSquash);
//        toggleZucchini = view.findViewById(R.id.toggleZucchini);
//        toggleBroccoli = view.findViewById(R.id.toggleBroccoli);
//        togglePeas = view.findViewById(R.id.togglePeas);
//        toggleCarrot = view.findViewById(R.id.toggleCarrot);
//        toggleBeans = view.findViewById(R.id.toggleBeans);
//        toggleOkra = view.findViewById(R.id.toggleOkra);
//        toggleBeanSprouts = view.findViewById(R.id.toggleBeanSprouts);
//        toggleTofu = view.findViewById(R.id.toggleTofu);
//        toggleBellPepper = view.findViewById(R.id.toggleBellPepper);
//        toggleEndive = view.findViewById(R.id.toggleEndive);
//        toggleCrickets = view.findViewById(R.id.toggleCrickets);
//        toggleMealworms = view.findViewById(R.id.toggleMealworms);
//        toggleGrasshoppers = view.findViewById(R.id.toggleGrasshoppers);
//        toggleEarthworms = view.findViewById(R.id.toggleEarthworms);
//        toggleCalciWorms = view.findViewById(R.id.toggleCalciWorms);
//        textCollardGreens = view.findViewById(R.id.ColGreensText);
//        textMustardGreens = view.findViewById(R.id.MustardGreensText);
//        textRomaine = view.findViewById(R.id.RomaineText);
//        textDandelion = view.findViewById(R.id.DandelionText);
//        textTurnipGreens = view.findViewById(R.id.TurnipGreensText);
//        textBokChoy = view.findViewById(R.id.BokChoyText);
//        textChicory = view.findViewById(R.id.ChicoryText);
//        textEscarole = view.findViewById(R.id.EscaroleText);
//        textWIldPlants = view.findViewById(R.id.WildPlantsText);
//        textCilantro = view.findViewById(R.id.CilantroText);
//        textWatercress = view.findViewById(R.id.WatercressText);
//        textGrapeLeaves = view.findViewById(R.id.GrapeLeavesText);
//        textSquash = view.findViewById(R.id.SquashText);
//        textZucchini = view.findViewById(R.id.ZucchiniText);
//        textBroccoli = view.findViewById(R.id.BroccoliText);
//        textPeas = view.findViewById(R.id.PeasText);
//        textCarrot = view.findViewById(R.id.CarrotText);
//        textBeans = view.findViewById(R.id.BeansText);
//        textOkra = view.findViewById(R.id.OkraText);
//        textBeanSprouts = view.findViewById(R.id.BeanSproutsText);
//        textTofu = view.findViewById(R.id.TofuText);
//        textBellPepper = view.findViewById(R.id.BellPepperText);
//        textEndive = view.findViewById(R.id.EndiveText);
//        textCrickets = view.findViewById(R.id.CricketsText);
//        textMealworms = view.findViewById(R.id.MealwormsText);
//        textGrasshoppers = view.findViewById(R.id.GrasshoppersText);
//        textEarthworms = view.findViewById(R.id.EarthwormsText);
//        textCalciWorms = view.findViewById(R.id.CalciWormsText);

        populateFoodList(view);

        view.findViewById(R.id.crickets);

        return view;
    }

    void checkExecution() {

        if(executed ==false){
        for (int j = 0; j < greensNames.length; j++) {
            dbFood.addItem("Leafy Green", greensNames[j], false);
        }
        for (int j = 0; j < vegNames.length; j++) {
            dbFood.addItem("Vegetable", vegNames[j], false);
        }
        for (int j = 0; j < bugNames.length; j++) {
            dbFood.addItem("Bug", bugNames[j], false);
        }}
        executed = true;
    }


    void populateFoodList(View view){
        LinearLayout greensDiv = (LinearLayout) view.findViewById(R.id.TypeLeafyGreens);
        LinearLayout vegDiv = (LinearLayout) view.findViewById(R.id.TypeVegetables);
        LinearLayout bugDiv = (LinearLayout) view.findViewById(R.id.TypeBugs);

        // TODO High Priority
        //  Once DB is implemented, build arrays from DB queries
        //  greensNames = SELECT NAME FROM FOOD WHERE TYPE = LEAFYGREEN

        for(String green : greensNames){
            View toAdd = getLayoutInflater().inflate(R.layout.food_bank_item, greensDiv, false);
            //toAdd.setId(idNumOfGreen);
            TextView itemText = (TextView) toAdd.findViewById(R.id.text);
            itemText.setText(green);
            greensDiv.addView(toAdd);
        }

        for(String veg : vegNames){
            View toAdd = getLayoutInflater().inflate(R.layout.food_bank_item, vegDiv, false);
            //toAdd.setId(idNumOfVeg);
            TextView itemText = (TextView) toAdd.findViewById(R.id.text);
            itemText.setText(veg);
            vegDiv.addView(toAdd);
        }

        for(String bug : bugNames){
            View toAdd = getLayoutInflater().inflate(R.layout.food_bank_item, bugDiv, false);
            //toAdd.setId(idNumOfBug);
            TextView itemText = (TextView) toAdd.findViewById(R.id.text);
            itemText.setText(bug);

            //Replace the ID from generic foodBankButton to ID specific to the bug name
            ToggleButton bugButton = (ToggleButton) toAdd.findViewById(R.id.foodBankButton);
            bugButton.setId(getResources().getIdentifier(bug, "integer", "values"));

            bugDiv.addView(toAdd);
            bugButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (bugButton.isChecked()){
                        dbFood.changeAvailabiliy(bugButton.getId());
                    }
                }
            });
        }




    }


}
