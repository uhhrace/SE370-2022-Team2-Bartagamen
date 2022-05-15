package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class DAO extends SQLiteOpenHelper{

    SimpleDateFormat dateFormatter = new SimpleDateFormat("mm/dd/yyyy", Locale.ENGLISH);

    private Context context;
    private static final String DATABASE_NAME = "Bartagamen.db";
    private static int DATABASE_VERSION = 1;

    private static final String drop = "DROP IF TABLE EXISTS ";

    private static DAO DAO = null;
    SQLiteDatabase BartDB = null;
    private DailyMealPlanEngine dmp = null;

    // Singleton object
    public static DAO getDAO() {
        if(DAO == null){
            //Do nothing
        }else{
            SQLiteDatabase t = DAO.getWritableDatabase();
            DAO.generateTables();
        }

        return DAO;
    }

    public void setContext(Context incomingContext){ this.context = incomingContext; }

    public DAO(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        DAO = this;

        SQLiteDatabase t = DAO.getWritableDatabase();
        dmp = DailyMealPlanEngine.getDMPEngine();
    }

    void generateTables(){
        generateFoodTable();
        getUserFoodList();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        BartDB = db;

        try{
            BartDB.execSQL(QUERY_GENERATE_FOOD_TABLE);
            BartDB.execSQL(QUERY_GENERATE_PET_TABLE);
            BartDB.execSQL(QUERY_GENERATE_MENU_TABLE);

            generateTables();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        BartDB = db;

    }

    ////////////////////////////////////////////////////////////////////
    //        ███████  ██████   ██████  ██████  ██████   █████   ██████
    //        ██      ██    ██ ██    ██ ██   ██ ██   ██ ██   ██ ██    ██
    //        █████   ██    ██ ██    ██ ██   ██ ██   ██ ███████ ██    ██
    //        ██      ██    ██ ██    ██ ██   ██ ██   ██ ██   ██ ██    ██
    //        ██       ██████   ██████  ██████  ██████  ██   ██  ██████
    ////////////////////////////////////////////////////////////////////

    final String QUERY_GENERATE_FOOD_TABLE = "CREATE TABLE " + TABLE_FOOD + "(" +
            COLUMN_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_FOOD_TYPE + " TEXT NOT NULL, " +
            COLUMN_FOOD_NAME + " TEXT NOT NULL, " +
            COLUMN_FOOD_AVAILABLE + " BOOLEAN NOT NULL);";

    static final String TABLE_FOOD = "food";
    static final String COLUMN_FOOD_ID = "_id";
    static final String COLUMN_FOOD_TYPE = "food_type";
    static final String COLUMN_FOOD_NAME = "name";
    static final String COLUMN_FOOD_AVAILABLE = "available";

    static JSONArray foodList = null;
    ArrayList<FoodItem> availableFoods = new ArrayList<>();

    public JSONArray getFoodList(){
        return foodList;
    }

    void generateFoodTable(){
        JSONObject foods;

        try{
            //Create Array of all Food Items from food_item_list.JSON
            InputStream stream = context.getResources().getAssets().open("food_item_list.json");
            Scanner scanner = new Scanner(stream);

            //Read JSON String from file
            String JSONString = scanner.useDelimiter("\\A").next();

            //Create JSON Object from JSON String
            foods = new JSONObject(JSONString);
            //Extract JSON Array from JSON Object
            //Build default food list, everything unavailable
            foodList = foods.getJSONArray("items");

            //Iterate through JSON Array, add each item to the DB
            for(int i = 0; i < foodList.length(); i++){
                addItem(
                        (int) foodList.getJSONObject(i).get("id"),
                        foodList.getJSONObject(i).get("type").toString(),
                        foodList.getJSONObject(i).get("name").toString(),
                        (boolean) foodList.getJSONObject(i).get("available"));
            }

        } catch(SQLiteConstraintException e){
            return;
        } catch (JSONException | IOException e){
            e.printStackTrace();
        }
    }

    public JSONArray getUserFoodList(){

        Cursor resultCursor = BartDB.rawQuery("SELECT * FROM " + TABLE_FOOD, null);
        resultCursor.moveToFirst();

        int id;
        int available;

        //While resultCursor.getPosition < resultCursor.getCount
        while (resultCursor.getPosition() < resultCursor.getCount() - 1){
            id = resultCursor.getInt(0);
            available = resultCursor.getInt(3);

            try{
//                foodList.getJSONObject(id).remove("available");
                if(1 == available){
                    foodList.getJSONObject(id).put("available", "true");
                }else{
                    foodList.getJSONObject(id).put("available", "false");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            resultCursor.moveToNext();
        }

        resultCursor.close();
        return foodList;
    }

    public ArrayList<FoodItem> getAvailableFoods(){
        return availableFoods;
    }

    public void updateAvailableFoods(){

        String[] columns = {COLUMN_FOOD_ID, COLUMN_FOOD_NAME, COLUMN_FOOD_TYPE, COLUMN_FOOD_AVAILABLE};
        String selection = "available == true";

        Cursor resultCursor = BartDB.query(true, TABLE_FOOD, columns, selection, null, null, null, null , null);
        resultCursor.moveToFirst();

        int INDEX_ID = 0;
        int INDEX_NAME = 1;
        int INDEX_TYPE = 2;

        while(resultCursor.getPosition() < resultCursor.getCount()){
            int id = resultCursor.getInt(INDEX_ID);
            String name = resultCursor.getString(INDEX_NAME);
            DailyMealPlanEngine.FoodType foodType = null;

            switch (resultCursor.getString(INDEX_TYPE)){
                case "LEAFYGREEN":
                    foodType = DailyMealPlanEngine.FoodType.LEAFYGREEN;
                    break;
                case "VEGETABLE":
                    foodType = DailyMealPlanEngine.FoodType.VEGETABLE;
                    break;
                case "PROTEIN":
                    foodType = DailyMealPlanEngine.FoodType.PROTEIN;
                    break;
            }

            FoodItem food = new FoodItem(id, foodType, name, true);
            availableFoods.add(food);

            resultCursor.moveToNext();
        }

        resultCursor.close();
    }

    public void addItem(int id, String type, String name, boolean available){
        BartDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FOOD_ID, id);
        cv.put(COLUMN_FOOD_TYPE, type);
        cv.put(COLUMN_FOOD_NAME, name);
        cv.put(COLUMN_FOOD_AVAILABLE, available);

        BartDB.insert(TABLE_FOOD, null, cv);
    }

    public void changeFoodAvailability(int id, boolean available){

        String query = "UPDATE " + TABLE_FOOD + " SET " + COLUMN_FOOD_AVAILABLE + "= " + available + " WHERE " + COLUMN_FOOD_ID + "= " + id;

        BartDB.execSQL(query);
        updateAvailableFoods();
    }

    ///////////////////////////////////////////////////////////
    //        ██████  ███████ ████████ ██████   █████   ██████
    //        ██   ██ ██         ██    ██   ██ ██   ██ ██    ██
    //        ██████  █████      ██    ██   ██ ███████ ██    ██
    //        ██      ██         ██    ██   ██ ██   ██ ██    ██
    //        ██      ███████    ██    ██████  ██   ██  ██████
    ///////////////////////////////////////////////////////////

    final String QUERY_GENERATE_PET_TABLE = "CREATE TABLE " + TABLE_PET + "(" +
            COLUMN_PET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PET_NAME + " TEXT NOT NULL, " +
            COLUMN_PET_SIZE + " TEXT NOT NULL, " +
            COLUMN_PET_DOB + " TEXT NOT NULL);";

    static final String TABLE_PET = "pet";
    static final String COLUMN_PET_ID = "_id";
    static final String COLUMN_PET_NAME = "name";
    static final String COLUMN_PET_SIZE = "size";
    static final String COLUMN_PET_DOB = "date_of_birth";

    public Lizard getLizard(String name){
        String[] columns = {COLUMN_PET_ID, COLUMN_PET_NAME, COLUMN_PET_SIZE, COLUMN_PET_DOB};
        String selection = COLUMN_PET_NAME + " == '" + name+ "'";

        Cursor resultCursor = BartDB.query(true, TABLE_PET, columns, selection, null, null, null, null , null);
        resultCursor.moveToFirst();

        Lizard newLizard = new Lizard(
                resultCursor.getInt(0),
                resultCursor.getString(1),
                resultCursor.getString(2),
                new Date(resultCursor.getString(3)));

        resultCursor.close();
        return newLizard;
    }

    public Lizard getLizard(int id){
        String[] columns = {COLUMN_PET_ID, COLUMN_PET_NAME, COLUMN_PET_SIZE, COLUMN_PET_DOB};
        String selection = COLUMN_PET_ID + " == " + id;

        Cursor resultCursor = BartDB.query(true, TABLE_PET, columns, selection, null, null, null, null , null);
        resultCursor.moveToFirst();

        Lizard newLizard = new Lizard(
                resultCursor.getInt(0),
                resultCursor.getString(1),
                resultCursor.getString(2),
                new Date(resultCursor.getString(3)));

        resultCursor.close();
        return newLizard;
    }

    public Lizard[] getLizards() throws ParseException, NullPointerException{
        String[] columns = {COLUMN_PET_ID, COLUMN_PET_NAME, COLUMN_PET_SIZE, COLUMN_PET_DOB};
        String selection = "TRUE";

        BartDB = DAO.getWritableDatabase();

        Lizard newLizard = null;

        Cursor resultCursor = BartDB.query(true, TABLE_PET, columns, selection,  null, null, null, null , null);
        Lizard[] lizards = new Lizard[resultCursor.getCount()];

        resultCursor.moveToFirst();

        while (resultCursor.getPosition() < resultCursor.getCount()){

            newLizard = new Lizard(
                    resultCursor.getInt(0),
                    resultCursor.getString(1),
                    resultCursor.getString(2),
                    dateFormatter.parse(resultCursor.getString(3))
            );

            lizards[resultCursor.getPosition()] = newLizard;
            resultCursor.moveToNext();
        }
        resultCursor.close();

        return lizards;
    }

    public String[] getLizardNames(){

        Lizard[] lizards = new Lizard[0];
        String[] names;

        try{
            lizards = getLizards();
        }catch (ParseException e){
            e.printStackTrace();
        }

        names = new String[lizards.length+2];

        names[0] = "Choose a Lizard";
        names[names.length-1] = "Add new Lizard";

        for(int i = 1; i < lizards.length+1; i++){
            names[i] = lizards[i-1].getName();
        }

        return names;
    }

    public void addPet(String name, String size, String age){
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PET_NAME, name);
        cv.put(COLUMN_PET_SIZE, size);
        cv.put(COLUMN_PET_DOB, age);

        BartDB.insert(TABLE_PET, null, cv);
    }

    // TODO High Priority Function
    //  Update pet when edit pet form completed
    public void updatePet(String name, String Size, String age){
        //stuff
    }

    ////////////////////////////////////////////////////////////////////////
    //        ███    ███ ███████ ███    ██ ██    ██ ██████   █████   ██████
    //        ████  ████ ██      ████   ██ ██    ██ ██   ██ ██   ██ ██    ██
    //        ██ ████ ██ █████   ██ ██  ██ ██    ██ ██   ██ ███████ ██    ██
    //        ██  ██  ██ ██      ██  ██ ██ ██    ██ ██   ██ ██   ██ ██    ██
    //        ██      ██ ███████ ██   ████  ██████  ██████  ██   ██  ██████
    ////////////////////////////////////////////////////////////////////////

    final String QUERY_GENERATE_MENU_TABLE = "CREATE TABLE " + TABLE_MENU + "(" +
                    //COLUMN_MENU_ID + " INTEGER AUTOINCREMENT, " +
                    COLUMN_MENU_DATE + " DATE NOT NULL, " +
                    COLUMN_MENU_PET_ID + " INTEGER NOT NULL, " +
                    COLUMN_MENU_FOOD_ID_LIST + " STRING NOT NULL, " +
                    "FOREIGN KEY (" + COLUMN_MENU_PET_ID + ")" +
                    "REFERENCES " + TABLE_PET + " (" + COLUMN_PET_ID + "), " +
                    "PRIMARY KEY (" + COLUMN_MENU_DATE + ", " + COLUMN_MENU_PET_ID + ")" +
                    ");";

    static final String TABLE_MENU = "menu";
    static final String COLUMN_MENU_DATE = "menu_date";
    static final String COLUMN_MENU_PET_ID = "pet_id";
    static final String COLUMN_MENU_FOOD_ID_LIST = "food_id";

    public MealPlan getMealPlan(int requestedPetId, Date requestedDate) throws JSONException{
        String[] columns = {COLUMN_MENU_PET_ID, COLUMN_MENU_DATE, COLUMN_MENU_FOOD_ID_LIST};
        String selection =
                COLUMN_MENU_PET_ID + " == " + requestedPetId + " AND " +
                COLUMN_MENU_DATE + " == '" + requestedDate + "'";

        Cursor resultCursor = BartDB.query(true, TABLE_MENU, columns, selection, null, null, null, null , null);
        resultCursor.moveToFirst();

        MealPlan requestedMealPlan;

        // If a meal plan exists for the pet at the given date
        if(resultCursor.getCount() >= 1){
            requestedMealPlan = new MealPlan(
                    resultCursor.getInt(0),
                    new Date(resultCursor.getString(1)),
                    new ArrayList<>() );

            JSONArray JSONFoodIds = new JSONArray(resultCursor.getString(2));
            for(int i = 0; i < JSONFoodIds.length(); i++){
                requestedMealPlan.addFoodId(JSONFoodIds.getInt(i));
            }
        }else{

            //Compare requestedDate to today's Date
            if(requestedDate.before(new Date())){
                //Asking for a previous date that has no Meal Plan, do not generate
                requestedMealPlan = null;
            }else{
                //There is no meal plan for the pet at the given date, create one
                requestedMealPlan = dmp.generateMealPlanForPetOnDate(requestedPetId, requestedDate);
            }
        }

        resultCursor.close();
        return requestedMealPlan;
    }

    public void addMealPlan(MealPlan planToAdd){
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_MENU_PET_ID, planToAdd.getPetId());
        cv.put(COLUMN_MENU_DATE, new Date(
                planToAdd.getDate().getYear(),
                planToAdd.getDate().getMonth(),
                planToAdd.getDate().getDate()   ).toString() );
        cv.put(COLUMN_MENU_FOOD_ID_LIST, new JSONArray(planToAdd.getFoodIdList()).toString());

        BartDB.insert(TABLE_MENU, null, cv);
    }

}
