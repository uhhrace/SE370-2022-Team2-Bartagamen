package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import java.util.Calendar;

public class EditPetScreenController extends BartScreenController {

    String name, date ;
    boolean invalidDate = false ;
    Calendar cal = Calendar.getInstance() ;
    int monthInt, dayInt, yearInt, DISPLAYED_PET_ID;

    AppCompatButton saveButton;
    AppCompatButton deleteButton ;
    EditText nameInput;
    TextView errorText;
    EditText dateInput;

    DAO dao;

    EditPetScreenController(){
        dao = DAO.getDAO();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_pet_screen, container, false);

        setButtonListeners(view);
        fillInfoFields();

        return view ;
    }

    @Override
    public void onResume() {
        super.onResume();

        fillInfoFields();
    }

    public void setDisplayedPetId(int newId){
        DISPLAYED_PET_ID = newId;
    }

    void fillInfoFields() {

        Lizard tempLizard = dao.getLizard(DISPLAYED_PET_ID) ;

        nameInput.setText(tempLizard.getName());
        String dateFormatted;
        String grabDay = tempLizard.getDateOfBirth().toString().substring(8,10) ;
        String grabYear = tempLizard.getDateOfBirth().toString().substring(24,28) ;
        String grabMonth = tempLizard.getDateOfBirth().toString().substring(4,7) ;

        switch (grabMonth) {
            case "Jan" : grabMonth = "01" ;
            break ;
            case "Feb" : grabMonth = "02" ;
            break ;
            case "Mar" : grabMonth = "03" ;
            break ;
            case "Apr" : grabMonth = "04" ;
            break ;
            case "May" : grabMonth = "05" ;
            break ;
            case "Jun" : grabMonth = "06" ;
            break ;
            case "Jul" : grabMonth = "07" ;
            break ;
            case "Aug" : grabMonth = "08" ;
            break ;
            case "Sep" : grabMonth = "09" ;
            break ;
            case "Oct" : grabMonth = "10" ;
            break ;
            case "Nov" : grabMonth = "11" ;
            break ;
            case "Dec" : grabMonth = "12" ;
            break ;
        }

            dateFormatted = grabMonth + "/" + grabDay + "/" + grabYear ;

            dateInput.setText(dateFormatted);
    }

    void setButtonListeners (View view){

        saveButton = view.findViewById(R.id.saveButton);
        deleteButton = view.findViewById(R.id.deleteButton) ;
        nameInput = view.findViewById(R.id.addLizardNameText);
        dateInput = view.findViewById(R.id.addLizardDateText) ;
        errorText = view.findViewById(R.id.errorText);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Remove pet from database
                dao.removePet(DISPLAYED_PET_ID);

                changeScreenToHome() ;
            }
        });


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
                    invalidDate = true ;
                }

                if (!invalidDate) {

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
                else if (invalidDate || date.length() > 10) {
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
                    // update pet in database
                    dao.updatePet(DISPLAYED_PET_ID, nameInput.getText().toString(), dateInput.getText().toString());

                    changeScreenToHome();
                    nameInput.getText().clear();
                    dateInput.getText().clear();
                }
                invalidDate = false ;
            }

        });
    }
}
