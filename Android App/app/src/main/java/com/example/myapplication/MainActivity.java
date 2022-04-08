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

    /**
     * @name    changeScreenToHome
     * @desc    Changes the main screen fragment to the home screen, and hides the top action bar.
     * @params  void
     * @returns void
     * @public
     */
    public void changeScreenToHome(){
        changeScreen(home);
        topBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.backgroundBlue)));
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
        topBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.backgroundBlue)));
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

        //TODO low priority
        // animation is choppy when this slides in, can we make this better?
        // TODO low priority
        //  this needs black text, the whole bar might need to be turned into a drawable object?
        topBar.setTitle("Lizard's Meal Plan");
        topBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

    }

    /**
     * @name    changeScreen
     * @desc    Changes the main screen fragment to a Fragment object passed in as a parameter
     * @params  destinationFragment Desired Fragment object to change the screen to
     * @returns void
     * @public
     */
    private void changeScreen(Fragment destinationFragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, destinationFragment).commit();
    }
}