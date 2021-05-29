package com.example.pets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.pets.R;
import com.example.pets.data.PetCursor;
import com.example.pets.data.petContract;
import com.example.pets.data.pet_db_helper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
private pet_db_helper mDbHelper;
private PetCursor mPetCursor;
private static final int LOADER_ID=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity


        mDbHelper=new pet_db_helper(this);
        ListView listView=(ListView) findViewById(R.id.list);

        mPetCursor=new PetCursor(this,null);
        listView.setAdapter(mPetCursor);

       // displayDatabaseInfo();
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent=new Intent(CatalogActivity.this,EditorActivity.class);

                Uri uri= ContentUris.withAppendedId(petContract.Shelter.CONTENT_URI,id);
                intent.setData(uri);
                startActivity(intent);

            }
        });
      LoaderManager.getInstance(this).initLoader(LOADER_ID,null,this);

    }


public void f(View view)
{
    Intent i=new Intent(CatalogActivity.this,EditorActivity.class);
    startActivity(i);
}

    public void insertPet()
    {
        ContentValues contentValues=new ContentValues();


        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        contentValues.put(petContract.Shelter.COLUMN_PET_NAME,"Toto");
        contentValues.put(petContract.Shelter.COLUMN_PET_BREED,"Terrier");
        contentValues.put(petContract.Shelter.COLUMN_PET_GENDER,petContract.Shelter.GENDER_MALE);
        contentValues.put(petContract.Shelter.COLUMN_PET_Weight,7);

        Uri newRowid= getContentResolver().insert(petContract.Shelter.CONTENT_URI,contentValues);
        Log.i(LOCATION_SERVICE,"new row id"+ newRowid);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                // Do nothing for now
          insertPet();
        // displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String projection[]={
          petContract.Shelter._ID,
          petContract.Shelter.COLUMN_PET_NAME,
          petContract.Shelter.COLUMN_PET_BREED
        };

        return new CursorLoader(this,petContract.Shelter.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
         mPetCursor.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mPetCursor.swapCursor(null);
    }
}