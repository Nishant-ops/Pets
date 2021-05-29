package com.example.pets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.pets.R;
import com.example.pets.data.petContract;
import com.example.pets.data.pet_db_helper;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private pet_db_helper mDbHelper;
    private EditText mNameEditText;

    /** EditText field to enter the pet's breed */
    private EditText mBreedEditText;

    /** EditText field to enter the pet's weight */
    private EditText mWeightEditText;

    /** EditText field to enter the pet's gender */
    private Spinner mGenderSpinner;

    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private int mGender = 0;
    private static final  int LOADER_ID=0;
    private Uri mC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent=getIntent();
        mC=intent.getData();

            if(mC==null) {
                setTitle(R.string.editor_activity_title_new_pet);
                Log.i(LOCATION_SERVICE,"GOING TO OPEN");
            }else {
                setTitle(R.string.editor_activity_title_edit_pet);
                LoaderManager.getInstance(this).initLoader(LOADER_ID,null,this);
            }


        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);
        mBreedEditText = (EditText) findViewById(R.id.edit_pet_breed);
        mWeightEditText = (EditText) findViewById(R.id.edit_pet_weight);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        setupSpinner();
        mDbHelper=new pet_db_helper(this);




    }

    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = petContract.Shelter.GENDER_MALE; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = petContract.Shelter.GENDER_FEMALE; // Female
                    } else {
                        mGender = petContract.Shelter.GENDER_UNKNOWN;  // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0; // Unknown
            }
        });
    }

    private void SavePet()
    {
       String name= mNameEditText.getText().toString();
       String breed=mBreedEditText.getText().toString();
       String w=mWeightEditText.getText().toString();
      // int weight=Integer.parseInt(w);

   if(mC==null&&TextUtils.isEmpty(name)&&
   }

        ContentValues contentValues=new ContentValues();


        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        contentValues.put(petContract.Shelter.COLUMN_PET_NAME,name);
        contentValues.put(petContract.Shelter.COLUMN_PET_BREED,breed);
        contentValues.put(petContract.Shelter.COLUMN_PET_GENDER,mGender);
        contentValues.put(petContract.Shelter.COLUMN_PET_Weight,weight);

        if(mC==null)
        {
            Uri newUri = getContentResolver().insert(petContract.Shelter.CONTENT_URI, contentValues);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mC,contentValues, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Do nothing for now
                SavePet();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
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
                petContract.Shelter.COLUMN_PET_BREED,
                petContract.Shelter.COLUMN_PET_GENDER,
                petContract.Shelter.COLUMN_PET_Weight
        };

        return new CursorLoader(this,mC,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
     if(data==null||data.getCount()<0)
         return;

        if (data.moveToFirst()) {
            int columnINDEX_GENDER = data.getColumnIndex(petContract.Shelter.COLUMN_PET_GENDER);
            int columnINDEX_NAME = data.getColumnIndex(petContract.Shelter.COLUMN_PET_NAME);
            int columnINDEX_BREED = data.getColumnIndex(petContract.Shelter.COLUMN_PET_BREED);
            int columnINDEX_WEIGHT = data.getColumnIndex(petContract.Shelter.COLUMN_PET_Weight);

            int weight = data.getInt(columnINDEX_WEIGHT);
            String name = data.getString(columnINDEX_NAME);
            String breed = data.getString(columnINDEX_NAME);
            int gender = data.getInt(columnINDEX_GENDER);

            mNameEditText.setText(name);
            mBreedEditText.setText(breed);
            mWeightEditText.setText(Integer.toString(weight));

            switch (gender) {
                case petContract.Shelter.GENDER_MALE:
                    mGenderSpinner.setSelection(1);
                    break;
                case petContract.Shelter.GENDER_FEMALE:
                    mGenderSpinner.setSelection(2);
                    break;
                default:
                    mGenderSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }
}