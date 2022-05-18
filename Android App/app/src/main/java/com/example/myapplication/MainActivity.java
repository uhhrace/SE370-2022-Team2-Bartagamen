package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    //declare bottomNavigationView
    BottomNavigationView bottomNavigationView;
    DAO DAO;
    DailyMealPlanEngine dmp;

    //declare home, calendar, pets, food in scope of this file
    public HomeScreenController homeScreenController;
    public CalendarScreenController calendarScreenController;
    public PetScreenController petScreenController;
    public FoodScreenController foodScreenController;

    public AddPetScreenController addPetScreenController ;

    private ActionBar topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //only light-mode --> disable dark-mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main);

        // Init singleton DAO object here
        DAO = new DAO(MainActivity.this);
        // We init the DB here
        SQLiteDatabase t = DAO.getWritableDatabase();

        // Init singleton DMP object here
        dmp = DailyMealPlanEngine.getDMPEngine();

        //initialize home, calendar, pets, food in scope of this file
        homeScreenController = new HomeScreenController();
        calendarScreenController = new CalendarScreenController();
        petScreenController = new PetScreenController();
        foodScreenController = new FoodScreenController();
        addPetScreenController = new AddPetScreenController();
        topBar = getSupportActionBar();

        petScreenController.attach(this);

        //initialize bottomNavigationView by connecting it to the xml-Element
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        //Don't call changeScreenToHome at this point, we don't need to call updateAvailableFoods
        changeScreen(homeScreenController);
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

    private void wipeTopBar() {
        topBar.setTitle(null);
        topBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.backgroundBlue)));
    }

    /**
     * @name changeScreenToHome
     * @desc Changes the main screen fragment to the home screen, and hides the top action bar.
     * @params void
     * @returns void
     * @public
     */
    public void changeScreenToHome() {
        changeScreen(homeScreenController);
        wipeTopBar();

        DAO.updateAvailableFoods();
    }

    /**
     * @name changeScreenToFoodBank
     * @desc Changes the main screen fragment to the Food Bank screen, and hides the top action bar
     * @params void
     * @returns void
     * @public
     */
    public void changeScreenToFoodBank() {
        changeScreen(foodScreenController);
        wipeTopBar();
    }

    /**
     * @name changeScreenToPets
     * @desc Changes the main screen fragment to the Individual Pets screen, and enables the top action bar
     * @params void
     * @returns void
     * @public
     */
    public void changeScreenToPets() {
        changeScreen(petScreenController);

        topBar.setTitle("Lizard's Meal Plan");
        topBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.buttonDarkBlue)));

        DAO.updateAvailableFoods();
    }

    public void changeScreenToPets(String petName) {
        changeScreen(petScreenController);

        petScreenController.setDisplayedPetId( DAO.getLizard(petName).getId() );

        topBar.setTitle(petName + "'s Meal Plan");
        topBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.buttonDarkBlue)));

        DAO.updateAvailableFoods();
    }

    public void changeScreenToCalendar() {
        changeScreen(calendarScreenController);
        topBar.setTitle("Lizard's Meal Plan - Monthly View");

        DAO.updateAvailableFoods();
    }


    public void changeScreenToAddPet() {
        changeScreen(addPetScreenController) ;
        topBar.setTitle("Create New Pet");
        topBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.buttonDarkBlue)));
    }

    /**
     * @name changeScreen
     * @desc Changes the main screen fragment to a Fragment object passed in as a parameter
     * @params destinationFragment Desired Fragment object to change the screen to
     * @returns void
     * @public
     */
    private void changeScreen(BartScreenController destinationScreen) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, destinationScreen).commit();
        //TODO low priority
        //It seems we need to call this every time we change screens, it'd be better if it was only
        // called once when we initialize the bartScreen objects inside onCreate()
        destinationScreen.attach(this);
    }

}
