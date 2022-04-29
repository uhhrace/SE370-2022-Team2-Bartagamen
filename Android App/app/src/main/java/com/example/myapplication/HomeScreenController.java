package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;

public class HomeScreenController extends BartScreenController {

    AppCompatSpinner petListButton;
    AppCompatButton foodBankButton;

    //When PetDAO is functional, replace this array with actual info from the DB
    class Lizard{
        private String name;
        Lizard(String newName){
            this.name = newName;
        }

        public String getName(){return this.name;}
        public void setName(String newName){this.name = newName;}
    }

    Lizard[] lizardList;
    String[] lizardNames;
    Object lastItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_screen, container, false);

        setButtonListeners(view);

        return view;
    }

    //When PetDAO is functional, replace this array with actual info from the DB
    void createLizardList(){
        lizardList = new Lizard[3];
        lizardNames = new String[lizardList.length+1];

        lizardList[0] = new Lizard("Shenron");
        lizardList[1] = new Lizard("Godzilla");
        lizardList[2] = new Lizard("Dragonite");

        lizardNames[0] = "Choose a lizard...";

        for (int i = 0; i < lizardList.length; i++) {
            lizardNames[i+1] = lizardList[i].getName();
        }
    }

    void setButtonListeners(View view){
        petListButton = view.findViewById(R.id.petListButton);
        foodBankButton = view.findViewById(R.id.foodBankButton);

        petListButton.setPrompt("Pet List");

        //Get petList
        //When PetDAO is functional, replace this array with actual info from the DB
        createLizardList();

        //Create adapter object for petList
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.lizards, R.layout.home);
        ArrayAdapter adapter = new ArrayAdapter(this.getContext(), R.layout.pet_list_spinner, lizardNames);
        //Attach adapter object to spinner
        petListButton.setAdapter(adapter);

        adapter.setDropDownViewResource(R.layout.pet_list_spinner);

        petListButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {

                if( (lastItem != petListButton.getItemAtPosition(position))
                    && (position > 0)
                ){
                    changeScreenToPets(petListButton.getItemAtPosition(position).toString());
                }
                lastItem = petListButton.getItemAtPosition(position);
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

    //TODO Bryce
    // changeScreenToAddPet, call changeScreen
}
