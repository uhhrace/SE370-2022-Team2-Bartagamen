package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    //declare bottomNavigationView
    BottomNavigationView bottomNavigationView;

    //declare home, calendar, pets, food in scope of this file
    public Home home;
    public Calender calendar;
    public Pets pets;
    public Food food;

    private ActionBar topBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize home, calendar, pets, food in scope of this file
        home = new Home();
        calendar = new Calender();
        pets = new Pets();
        food = new Food();
        topBar = getSupportActionBar();

        pets.attach(this);

        //initialize bottomNavigationView by connecting it to the xml-Element
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        changeScreenToHome();
    }

    // TODO low priority
    //  OnNavigationItemSelectedListener is deprecated, find current version of this listener
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            //set selected screen to declared public screen
            switch (item.getItemId()) {
                case R.id.bottom_home_button:
                    changeScreenToHome();
                    break;
                case R.id.bottom_pets_button:
                    changeScreenToPets();
                    break;
                case R.id.bottom_food_bank_button:
                    changeScreenToFoodBank();
                    break;
            }

            return true;
        }
    };

    private void wipeTopBar(){
        topBar.setTitle(null);
        topBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.backgroundBlue)));
    }

    /**
     * @name    changeScreenToHome
     * @desc    Changes the main screen fragment to the home screen, and hides the top action bar.
     * @params  void
     * @returns void
     * @public
     */
    public void changeScreenToHome(){
        changeScreen(home);
        wipeTopBar();
    }

    /**
     * @name    changeScreenToFoodBank
     * @desc    Changes the main screen fragment to the Food Bank screen, and hides the top action bar
     * @params  void
     * @returns void
     * @public
     */
    public void changeScreenToFoodBank(){
        changeScreen(food);
        wipeTopBar();
    }

    /**
     * @name    changeScreenToPets
     * @desc    Changes the main screen fragment to the Individual Pets screen, and enables the top action bar
     * @params  void
     * @returns void
     * @public
     */
    public void changeScreenToPets(){
        changeScreen(pets);

        //TODO very low priority
        // Currently, text defaults to white, so the topBar is made buttonDarkBlue for easy reading
        // Ideally the top bar would be backgroundBlue, and the text would be Black, but there is
        // no easy option for setting the text color. The topBar object might have to be converted
        // to a View / Fragment?
        topBar.setTitle("Lizard's Meal Plan");
        topBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.buttonDarkBlue)));
    }

    public void changeScreenToPets(String petName){
        changeScreen(pets);

        topBar.setTitle(petName + "'s Meal Plan");
        topBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.buttonDarkBlue)));
    }

    public void changeScreenToCalendar(){
        changeScreen(calendar);
        topBar.setTitle("Lizard's Meal Plan - Monthly View");
    }

    /**
     * @name    changeScreen
     * @desc    Changes the main screen fragment to a Fragment object passed in as a parameter
     * @params  destinationFragment Desired Fragment object to change the screen to
     * @returns void
     * @public
     */
    private void changeScreen(BartScreen destinationScreen){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, destinationScreen).commit();

        //TODO low priority
        //It seems we need to call this every time we change screens, it'd be better if it was only
        // called once when we initialize the bartScreen objects inside onCreate()
        destinationScreen.attach(this);
    }
}