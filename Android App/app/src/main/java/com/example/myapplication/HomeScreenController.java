package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import org.json.JSONException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class HomeScreenController extends BartScreenController {

    AppCompatSpinner petListButton;
    AppCompatButton foodBankButton;
    DAO dao;

    Lizard[] lizardList = null;
    String[] lizardNames = null;

    public HomeScreenController(){
        dao = DAO.getDAO();
        SQLiteDatabase t = dao.getWritableDatabase();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_screen, container, false);

        setButtonListeners(view);

        try{
            lizardList = dao.getLizards();
            lizardNames = dao.getLizardNames();
        }catch (ParseException e){
            e.printStackTrace();
        }

        updateMealPlanDisplay(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateMealPlanDisplay( (ViewGroup)getView().getParent() );
    }

    void setButtonListeners(View view){
        petListButton = view.findViewById(R.id.petListButton);
        foodBankButton = view.findViewById(R.id.foodBankButton);

        petListButton.setPrompt("Pet List");

        //Get petList
        //When PetDAO is functional, replace this array with actual info from the DB
        lizardNames = dao.getLizardNames();


        //Create adapter object for petList
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.lizards, R.layout.home);
        ArrayAdapter adapter = new ArrayAdapter(this.getContext(), R.layout.pet_list_spinner, lizardNames);
        //Attach adapter object to spinner
        petListButton.setAdapter(adapter);
        petListButton.setSaveEnabled(false);

        adapter.setDropDownViewResource(R.layout.pet_list_spinner);

        petListButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {

                if(position == (lizardNames.length-1)){
                    changeScreenToAddPet();
                }else if( (position > 0) && (position < (lizardNames.length-1)) ) {
                    // send the name of the selected lizard to individual pet screen
                    changeScreenToPets(petListButton.getItemAtPosition(position).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView adapterView) {
                //nothing
            }
        });

        foodBankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeScreenToFoodBank();
            }
        });

    }

    void updateMealPlanDisplay(View view){

        TextView mealPlanSumDisplay = view.findViewById(R.id.DailyMenuSumTextView);
        String mealPlanSumText;
        Date today = new Date();
        Date simpleToday = new Date(today.getYear(), today.getMonth(), today.getDate());

        try{
            ArrayList<FoodItem> todaysFoods = dao.getFoodsForDate(simpleToday);
            mealPlanSumText = "Daily Food Sum:\n\t";

            for (FoodItem food : todaysFoods) {
                mealPlanSumText += food.getName();
                mealPlanSumText += "\n\t";
            }

        }catch (JSONException e){
            mealPlanSumText = "No food on record today";
        }

        mealPlanSumDisplay.setText(mealPlanSumText);
    }
}
