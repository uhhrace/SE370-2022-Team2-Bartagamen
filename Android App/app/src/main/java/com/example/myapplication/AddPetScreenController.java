package com.example.myapplication;

//maybe import stuff here idk

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import java.util.Calendar;

public class AddPetScreenController extends BartScreenController {

    String name, date;
    boolean test = false ;
    Calendar cal = Calendar.getInstance() ;
    int monthInt, dayInt, yearInt ;

    AppCompatButton saveButton;
    EditText nameInput;
    TextView errorText;
    EditText dateInput ;

    DAO dao ;

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


            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    name = nameInput.getText().toString() ;
                    date = dateInput.getText().toString() ;

                    //check if day month and year are numbers
                    try {
                        String month = date.substring(0,2) ;
                        String day = date.substring(3,5);
                        String year = date.substring(6,10);

                        int monthInt = Integer.parseInt(month) ;
                        int dayInt = Integer.parseInt(day) ;
                        int yearInt = Integer.parseInt(year) ;

                    }
                    catch (Exception e) {
                        test = true ;
                    }

                    if (!test) {

                        String month = date.substring(0, 2);
                        String day = date.substring(3, 5);
                        String year = date.substring(6, 10);

                        monthInt = Integer.parseInt(month);
                        dayInt = Integer.parseInt(day);
                        yearInt = Integer.parseInt(year);
                    }

                    //check empty
                    if (name.isEmpty() || date.isEmpty()) {
                        errorText.setText("Error. Some fields empty.");
                    }
                    //check length
                    else if (test || date.length() > 10) {
                        errorText.setText("Error. Date of Birth is incorrect or incomplete");
                    }
                    //check if day month or year are real numbers for a date, also check if year is in the future
                    else if (monthInt > 12 || monthInt < 1 || dayInt > 31 || dayInt < 1 || yearInt < 2000 || yearInt > cal.get(Calendar.YEAR)) {
                        errorText.setText("Error. Date of Birth is incorrect or incomplete");
                    }
                    //check if the month is in the future
                    else if (yearInt == cal.get(Calendar.YEAR) && monthInt > cal.get(Calendar.MONTH) + 1) {
                        errorText.setText("Error. Date of Birth is incorrect or incomplete");
                    }
                    //check if the day is in the future
                    else if (yearInt == cal.get(Calendar.YEAR) && monthInt == cal.get(Calendar.MONTH) + 1 && dayInt > cal.get(Calendar.DAY_OF_MONTH)) {
                        errorText.setText("Error. Date of Birth is incorrect or incomplete");
                    }
                    //check if the / symbol is correct
                    else if (date.charAt(2) != '/' || date.charAt(5) != '/') {
                        errorText.setText("Error. Date of Birth is incorrect or incomplete");
                    }
                    else {

                        //dao.addPet(name, "blank", date) ;

                        changeScreenToHome();
                        nameInput.getText().clear();
                        dateInput.getText().clear();
                    }
                    test = false ;
                }
            });
        }
    }

