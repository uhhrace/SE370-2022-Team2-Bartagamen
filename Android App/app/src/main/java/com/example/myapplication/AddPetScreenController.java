package com.example.myapplication;

//maybe import stuff here idk

import java.time.Instant;
import java.util.Calendar;
import java.util.TimeZone;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;

public class AddPetScreenController extends BartScreenController {

    String name, date;

    private DatePickerDialog datePickerDialog;
    private Button dateButton;


    AppCompatButton saveButton;
    EditText nameInput;
    TextView errorText;
    EditText dateInput ;
    //Declare Lizard object

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_pet_screen, container, false);

        setButtonListeners(view);

        return view ;

    }


        void setButtonListeners (View view){

            saveButton = view.findViewById(R.id.saveButton);
            nameInput = view.findViewById(R.id.addLizardNameText);
            dateInput = view.findViewById(R.id.addLizardDateText) ;
            errorText = view.findViewById(R.id.errorText);


            //TODO Bryce
            // Set saveButton listener
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //          //TODO Bryce
                    //          //read text forms for required info
                    name = nameInput.getText().toString() ;
                    date = dateInput.getText().toString() ;
                    //
                    // if info incomplete
                    //TODO
                    // Set up 3 spinners with month day and year
                    if (name.isEmpty() || date.isEmpty()) {
                        //      error, ask for more info
                        errorText.setText("Error. Some fields are incorrect or incomplete.");
                    } else {
                        changeScreenToHome();
                        nameInput.getText().clear();
                        dateInput.getText().clear();
                        //      build Lizard object based on textform info
                        //      send lizard to DB
                        //
                    }

                }
            });
        }
    }

