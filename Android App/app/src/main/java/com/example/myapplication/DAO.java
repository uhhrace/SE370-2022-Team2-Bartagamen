package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class DAO extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Bartagamen.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_FOOD = "food";
    private static final String COLUMN_FOOD_ID = "_id";
    private static final String COLUMN_FOOD_TYPE = "food_type";
    private static final String COLUMN_FOOD_NAME = "name";
    private static final String COLUMN_FOOD_AVAILABLE = "available";

    private static final String TABLE_PET = "pet";
    private static final String COLUMN_PET_ID = "_id";
    private static final String COLUMN_PET_NAME = "name";
    private static final String COLUMN_PET_SIZE = "size";
    private static final String COLUMN_PET_DOB = "date_of_birth";

    private static final String TABLE_MENU = "menu";
    private static final String COLUMN_MENU_ID = "_id";
    private static final String COLUMN_MENU_DATE = "menu_date";
    private static final String COLUMN_MENU_PET_ID = "pet_id";
    private static final String COLUMN_MENU_FOOD_ID = "food_id";

    final String QUERY_GENERATE_FOOD_TABLE = "CREATE TABLE " + TABLE_FOOD + "(" +
            COLUMN_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_FOOD_TYPE + " TEXT NOT NULL, " +
            COLUMN_FOOD_NAME + " TEXT NOT NULL, " +
            COLUMN_FOOD_AVAILABLE + " BOOLEAN NOT NULL);";

    final String QUERY_GENERATE_PET_TABLE = "CREATE TABLE " + TABLE_PET + "(" +
            COLUMN_PET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PET_NAME + " TEXT NOT NULL, " +
            COLUMN_PET_SIZE + " TEXT NOT NULL, " +
            COLUMN_PET_DOB + " TEXT NOT NULL);";

    final String QUERY_GENERATE_MEAL_TABLE = "CREATE TABLE " + TABLE_MENU + "(" +
                COLUMN_MENU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MENU_DATE + " DATE NOT NULL, " +
                COLUMN_MENU_PET_ID + " INTEGER NOT NULL, " +
                COLUMN_MENU_FOOD_ID + " INTEGER NOT NULL);";
    private static final String drop = "DROP IF TABLE EXISTS ";

    private static DAO DAO = null;

    private final String STRING_TRUE = "true";
    private final String STRING_FALSE = "false";

    SQLiteDatabase BartDB = null;
    JSONArray foodList = null;
    ArrayList<FoodItem> availableFoods = new ArrayList<>();

    // Singleton object
    public static DAO getDAO() {
        if(DAO == null){
            //Do nothing
        }
        return DAO;
    }

    public DAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        DAO = this;

        generateTables();
    }

    public JSONArray getFoodList(){
        return foodList;
    }

    void generateFoodTable(){
        JSONObject foods;

        try{
            //Create Array of all Food Items from food_item_list.JSON
            InputStream stream = this.context.getResources().getAssets().open("food_item_list.json");
            Scanner scanner = new Scanner(stream);

            //Read JSON String from file
            String JSONString = scanner.useDelimiter("\\A").next();

            //Create JSON Object from JSON String
            foods = new JSONObject(JSONString);
            //Extract JSON Array from JSON Object
            foodList = foods.getJSONArray("items");

            //Iterate through JSON Array, add each item to the DB
            for(int i = 0; i < foodList.length(); i++){
                DAO.addItem(
                        (int) foodList.getJSONObject(i).get("id"),
                        foodList.getJSONObject(i).get("type").toString(),
                        foodList.getJSONObject(i).get("name").toString(),
                        (boolean) foodList.getJSONObject(i).get("available"));
            }
        } catch (JSONException | IOException e){
            e.printStackTrace();
        }
    }

    public ArrayList getAvailableFoods(){
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
    }

    void generateTables(){
        generateFoodTable();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        BartDB = db;

        try{
            BartDB.execSQL(QUERY_GENERATE_FOOD_TABLE);
            BartDB.execSQL(QUERY_GENERATE_PET_TABLE);
            BartDB.execSQL(QUERY_GENERATE_MEAL_TABLE);
        }catch(SQLException ex){
            Log.e("Error", "Creation of the table failed" + ex);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        BartDB = db;

        BartDB.execSQL(drop + TABLE_FOOD);
        BartDB.execSQL(drop + TABLE_PET);
        db.execSQL(drop + TABLE_MENU);
        onCreate(db);
    }

    public void addItem(int id, String type, String name, boolean available){
        BartDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FOOD_ID, id);
        cv.put(COLUMN_FOOD_TYPE, type);
        cv.put(COLUMN_FOOD_NAME, name);
        cv.put(COLUMN_FOOD_AVAILABLE, available);

        long result = BartDB.insert(TABLE_FOOD, null, cv);
    }

    public void changeFoodAvailability(int id, boolean available){

        String query = "UPDATE " + TABLE_FOOD + " SET " + COLUMN_FOOD_AVAILABLE + "= " + available + " WHERE " + COLUMN_FOOD_ID + "= " + id;

        BartDB.execSQL(query);
        updateAvailableFoods();
    }

    public Lizard getLizard(String name){
        String[] columns = {COLUMN_PET_ID, COLUMN_PET_NAME, COLUMN_PET_SIZE, COLUMN_PET_DOB};
        String selection = COLUMN_PET_NAME + " == '" + name+ "'";

        Cursor resultCursor = BartDB.query(true, TABLE_PET, columns, selection, null, null, null, null , null);
        resultCursor.moveToFirst();

        Lizard newLizard = new Lizard(
                resultCursor.getInt(0),
                resultCursor.getString(1),
                resultCursor.getInt(2),
                new Date(resultCursor.getString(3)));

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
                resultCursor.getInt(2),
                new Date(resultCursor.getString(3)));

        return newLizard;
    }

    public void addPet(int id, String name, String size, String age){
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PET_ID, id);
        cv.put(COLUMN_PET_NAME, name);
        cv.put(COLUMN_PET_SIZE, size);
        cv.put(COLUMN_PET_DOB, age);

        long result = BartDB.insert(TABLE_PET, null, cv);
    }

    // TODO High Priority Function
    //  Update pet when edit pet form completed
    public void updatePet(String name, String Size, String age){
        //stuff
    }
}
