package com.example.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class pet_db_helper extends SQLiteOpenHelper {
private static final String LOG_TAG=petContract.class.getName();
    private static  int DATABASE_VERSION=1;
    private static  String DATABASE_NAME="shelter.db";

    public pet_db_helper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String PET_ENTRY_TABLE="Create TABLE "+petContract.Shelter.TABLE_NAME+" ("
                +petContract.Shelter._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +petContract.Shelter.COLUMN_PET_NAME+" TEXT NOT NULL, "
                +petContract.Shelter.COLUMN_PET_BREED+" TEXT, "
                +petContract.Shelter.COLUMN_PET_GENDER+" INTEGER NOT NULL, "
                +petContract.Shelter.COLUMN_PET_Weight+" INTEGER NOT NULL DEFAULT 0);";
     sqLiteDatabase.execSQL(PET_ENTRY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.setVersion(oldVersion);
    }
}
