package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PetDAO extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Bartagamen.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PET = "pet";
    private static final String COLUMN_PET_ID = "_id";
    private static final String COLUMN_PET_NAME = "name";
    private static final String COLUMN_PET_SIZE = "size";
    private static final String COLUMN_PET_AGE = "date_of_birth";

    final String query = "CREATE TABLE " + TABLE_PET + "(" +
            COLUMN_PET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PET_NAME + " TEXT NOT NULL, " +
            COLUMN_PET_SIZE + " TEXT NOT NULL, " +
            COLUMN_PET_AGE + " TEXT NOT NULL);";

    private static final String drop = "DROP IF TABLE EXISTS ";


    public PetDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(query);

        }catch(SQLException ex){
            Log.e("Error", "Creation of the table failed" + ex);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL(drop + TABLE_PET);
            onCreate(db);

        }

    public void addPet(String name, String size, String age){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PET_NAME, name);
        cv.put(COLUMN_PET_SIZE, size);
        cv.put(COLUMN_PET_AGE, age);

        long result = db.insert(TABLE_PET, null, cv);


    }
}
