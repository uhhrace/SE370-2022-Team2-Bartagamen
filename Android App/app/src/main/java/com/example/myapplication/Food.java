package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Food extends BartScreen {

    MyDatabaseHelper dbFood;


    ToggleButton toggleCollardGreens, toggleMustardGreens, toggleRomaine, toggleDandelion, toggleTurnipGreens, toggleBokChoy,
            toggleChicory, toggleEscarole, toggleWIldPlants, toggleCilantro, toggleWatercress, toggleGrapeLeaves, toggleSquash,
            toggleZucchini, toggleBroccoli, togglePeas, toggleCarrot, toggleBeans, toggleOkra, toggleBeanSprouts, toggleTofu,
            toggleBellPepper, toggleEndive, toggleCrickets, toggleMealworms, toggleGrasshoppers, toggleEarthworms, toggleCalciWorms;

    TextView textCollardGreens, textMustardGreens, textRomaine, textDandelion, textTurnipGreens, textBokChoy,
            textChicory, textEscarole, textWIldPlants, textCilantro, textWatercress, textGrapeLeaves, textSquash,
            textZucchini, textBroccoli, textPeas, textCarrot, textBeans, textOkra, textBeanSprouts, textTofu,
            textBellPepper, textEndive, textCrickets, textMealworms, textGrasshoppers, textEarthworms, textCalciWorms;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.food, container, false);
        toggleCollardGreens = view.findViewById(R.id.toggleColGreens);
        toggleMustardGreens = view.findViewById(R.id.toggleMustardGreens);
        toggleRomaine = view.findViewById(R.id.toggleRomaine);
        toggleDandelion = view.findViewById(R.id.toggleDandelion);
        toggleTurnipGreens = view.findViewById(R.id.toggleTurnipGreens);
        toggleBokChoy = view.findViewById(R.id.toggleBokChoy);
        toggleChicory = view.findViewById(R.id.toggleChicory);
        toggleEscarole = view.findViewById(R.id.toggleEscarole);
        toggleWIldPlants = view.findViewById(R.id.toggleWildPlants);
        toggleCilantro = view.findViewById(R.id.toggleCilantro);
        toggleWatercress = view.findViewById(R.id.toggleWatercress);
        toggleGrapeLeaves = view.findViewById(R.id.toggleGrapeLeaves);
        toggleSquash = view.findViewById(R.id.toggleSquash);
        toggleZucchini = view.findViewById(R.id.toggleZucchini);
        toggleBroccoli = view.findViewById(R.id.toggleBroccoli);
        togglePeas = view.findViewById(R.id.togglePeas);
        toggleCarrot = view.findViewById(R.id.toggleCarrot);
        toggleBeans = view.findViewById(R.id.toggleBeans);
        toggleOkra = view.findViewById(R.id.toggleOkra);
        toggleBeanSprouts = view.findViewById(R.id.toggleBeanSprouts);
        toggleTofu = view.findViewById(R.id.toggleTofu);
        toggleBellPepper = view.findViewById(R.id.toggleBellPepper);
        toggleEndive = view.findViewById(R.id.toggleEndive);
        toggleCrickets = view.findViewById(R.id.toggleCrickets);
        toggleMealworms = view.findViewById(R.id.toggleMealworms);
        toggleGrasshoppers = view.findViewById(R.id.toggleGrasshoppers);
        toggleEarthworms = view.findViewById(R.id.toggleEarthworms);
        toggleCalciWorms = view.findViewById(R.id.toggleCalciWorms);
        textCollardGreens = view.findViewById(R.id.ColGreensText);
        textMustardGreens = view.findViewById(R.id.MustardGreensText);
        textRomaine = view.findViewById(R.id.RomaineText);
        textDandelion = view.findViewById(R.id.DandelionText);
        textTurnipGreens = view.findViewById(R.id.TurnipGreensText);
        textBokChoy = view.findViewById(R.id.BokChoyText);
        textChicory = view.findViewById(R.id.ChicoryText);
        textEscarole = view.findViewById(R.id.EscaroleText);
        textWIldPlants = view.findViewById(R.id.WildPlantsText);
        textCilantro = view.findViewById(R.id.CilantroText);
        textWatercress = view.findViewById(R.id.WatercressText);
        textGrapeLeaves = view.findViewById(R.id.GrapeLeavesText);
        textSquash = view.findViewById(R.id.SquashText);
        textZucchini = view.findViewById(R.id.ZucchiniText);
        textBroccoli = view.findViewById(R.id.BroccoliText);
        textPeas = view.findViewById(R.id.PeasText);
        textCarrot = view.findViewById(R.id.CarrotText);
        textBeans = view.findViewById(R.id.BeansText);
        textOkra = view.findViewById(R.id.OkraText);
        textBeanSprouts = view.findViewById(R.id.BeanSproutsText);
        textTofu = view.findViewById(R.id.TofuText);
        textBellPepper = view.findViewById(R.id.BellPepperText);
        textEndive = view.findViewById(R.id.EndiveText);
        textCrickets = view.findViewById(R.id.CricketsText);
        textMealworms = view.findViewById(R.id.MealwormsText);
        textGrasshoppers = view.findViewById(R.id.GrasshoppersText);
        textEarthworms = view.findViewById(R.id.EarthwormsText);
        textCalciWorms = view.findViewById(R.id.CalciWormsText);


        dbFood = new MyDatabaseHelper(getContext());
        dbFood.addItem("Test", "Test", false);


        return view;
    }
}


