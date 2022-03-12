package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    //declare bottomNavigationView
    BottomNavigationView bottomNavigationView;

    //declare home, calendar, pets, food in scope of this file
    public Home home;
    public Calender calendar;
    public Pets pets;
    public Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize home, calendar, pets, food in scope of this file
        home = new Home();
        calendar = new Calender();
        pets = new Pets();
        food = new Food();

        //initialize bottomNavigationView by connecting it to the xml-Element
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        //Display Home Screen at the beginning
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, home).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;


            //set selected screen to declared public screen
            switch (item.getItemId()) {
                case R.id.item1:
                    selectedFragment = home;
                    break;
                case R.id.item2:
                    selectedFragment = calendar;
                    break;
                case R.id.item3:
                    selectedFragment = pets;
                    break;
                case R.id.item4:
                    selectedFragment = food;
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
            return true;
        }
    };

}