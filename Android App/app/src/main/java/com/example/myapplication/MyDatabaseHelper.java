package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Bartagamen.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_FOOD = "food";
    private static final String COLUMN_FOOD_ID = "_id";
    private static final String COLUMN_FOOD_TYPE = "food_type";
    private static final String COLUMN_FOOD_NAME = "name";
    // TODO High Priority
    //  Do we need a "Type" field for these Food items? Veg, Greens, Protein?
    private static final String COLUMN_FOOD_AVAILABLE = "available";

    private static final String TABLE_PET = "pet";
    private static final String COLUMN_PET_ID = "_id";
    private static final String COLUMN_PET_NAME = "name";
    private static final String COLUMN_PET_SIZE = "size";
    private static final String COLUMN_PET_AGE = "age";

    private static final String TABLE_MENU = "menu";
    private static final String COLUMN_MENU_ID = "_id";
    private static final String COLUMN_MENU_DATE = "menu_date";
    private static final String COLUMN_MENU_PET_ID = "pet_id";
    private static final String COLUMN_MENU_FOOD_ID = "food_id";

    final String query1 = "CREATE TABLE " + TABLE_FOOD + "(" +
            COLUMN_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_FOOD_TYPE + " TEXT NOT NULL, " +
            COLUMN_FOOD_NAME + " TEXT NOT NULL, " +
            COLUMN_FOOD_AVAILABLE + " TEXT NOT NULL);";

    private static final String drop = "DROP IF TABLE EXISTS ";


    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


      /*  String query2 = "CREATE TABLE " + TABLE_PET + "(" +
                COLUMN_PET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PET_NAME + " TEXT NOT NULL, " +
                COLUMN_PET_SIZE + " TEXT NOT NULL, " +
                COLUMN_PET_AGE + " TEXT NOT NULL);";

        String query3 = "CREATE TABLE " + TABLE_MENU + "(" +
                COLUMN_MENU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MENU_DATE + " DATE NOT NULL, " +
                COLUMN_MENU_PET_ID + " INTEGER NOT NULL, " +
                COLUMN_MENU_FOOD_ID + " INTEGER NOT NULL);";  */

        try{
            db.execSQL(query1);
           // db.execSQL(query2);
         //   db.execSQL(query3);

        }catch(SQLException ex){
            Log.e("Error", "Creation of the table failed" + ex);
        }



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(drop + TABLE_FOOD);
      //  db.execSQL(drop + TABLE_PET);
      //  db.execSQL(drop + TABLE_MENU);
        onCreate(db);

    }

    public void addItem(String type, String name, boolean available){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FOOD_TYPE, type);
        cv.put(COLUMN_FOOD_NAME, name);
        cv.put(COLUMN_FOOD_AVAILABLE, available);

        long result = db.insert(TABLE_FOOD, null, cv);


    }
}
