package com.example.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class PetProvider extends ContentProvider {
    public static final String LOG_TAG = PetProvider.class.getSimpleName();
    private pet_db_helper petDbHelper;
    private static final int PETS=100;
    private static final int PETS_ID=101;

    private static final UriMatcher sUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(petContract.CONTENT_AUTHORITY,petContract.PATH_PETS,PETS);

        sUriMatcher.addURI(petContract.CONTENT_AUTHORITY,petContract.PATH_PETS+"/#",PETS_ID);
    }
    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        // TODO: Create and initialize a PetDbHelper object to gain access to the pets database.
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        petDbHelper=new pet_db_helper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase sqLiteDatabase = petDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case PETS:

                cursor = sqLiteDatabase.query(petContract.Shelter.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case PETS_ID:

                selection = petContract.Shelter._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = sqLiteDatabase.query(petContract.Shelter.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query Unknown error" + uri);
        }
           cursor.setNotificationUri(getContext().getContentResolver(),petContract.Shelter.CONTENT_URI);
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int match=sUriMatcher.match(uri);

        switch(match)
        {
            case PETS:

              return  insertPet(uri,contentValues);

            default:
                throw new IllegalArgumentException();
        }


        }
  private boolean isvalid(Integer gender)
  {
      if(gender==petContract.Shelter.GENDER_MALE||gender==petContract.Shelter.GENDER_FEMALE||gender==petContract.Shelter.GENDER_UNKNOWN)
      return true;

      return false;
  }
     private Uri insertPet(Uri uri,ContentValues contentValues)
     {
         String name=contentValues.getAsString(petContract.Shelter.COLUMN_PET_NAME);

         if(name==null)
         {
             throw new IllegalArgumentException("pet requires a name");
         }

         Integer gender=contentValues.getAsInteger(petContract.Shelter.COLUMN_PET_GENDER);

         if(gender==null||isvalid(gender)==false)
         {
             throw new IllegalArgumentException("pet requires valid gender");
         }

         Integer weight=contentValues.getAsInteger(petContract.Shelter.COLUMN_PET_Weight);

         if(weight==null||weight<0)
         {
             throw new IllegalArgumentException("pet requies valid weight");
         }
         SQLiteDatabase sqLiteDatabase=petDbHelper.getWritableDatabase();

         long id=sqLiteDatabase.insert(petContract.Shelter.TABLE_NAME,null,contentValues);

         if(id==-1)
         {
             Log.e(LOG_TAG,"failed");
             return null;
         }
         getContext().getContentResolver().notifyChange(uri,null);
         return ContentUris.withAppendedId(uri, id);
     }
    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int match=sUriMatcher.match(uri);

        switch(match)
        {
            case PETS:

                return UpdateContents(uri,contentValues,selection,selectionArgs);


            case PETS_ID:

                selection=petContract.Shelter._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                return UpdateContents(uri,contentValues,selection,selectionArgs);


            default:
                throw  new IllegalArgumentException();
        }
    }


    private int UpdateContents(Uri uri,ContentValues contentValues,String selection,String[] selectionArgs)
    {
        if(contentValues.containsKey(petContract.Shelter.COLUMN_PET_NAME))
        {
            String name=contentValues.getAsString(petContract.Shelter.COLUMN_PET_NAME);

            if(name==null)
            {
                throw new IllegalArgumentException("pet requires a name");
            }
        }

        if(contentValues.containsKey(petContract.Shelter.COLUMN_PET_GENDER))
        {
            Integer gender=contentValues.getAsInteger(petContract.Shelter.COLUMN_PET_GENDER);

            if(gender==null||isvalid(gender)==false)
            {
                throw new IllegalArgumentException("pet requires valid gender");
            }
        }

        if(contentValues.containsKey(petContract.Shelter.COLUMN_PET_Weight))
        {
            Integer weight=contentValues.getAsInteger(petContract.Shelter.COLUMN_PET_Weight);

            if(weight==null||weight<0)
            {
                throw new IllegalArgumentException("pet requies valid weight");
            }
        }
        SQLiteDatabase sqLiteDatabase=petDbHelper.getWritableDatabase();
        int newROw=sqLiteDatabase.update(petContract.Shelter.TABLE_NAME,contentValues,selection,selectionArgs);

        if(newROw==-1)
        {
            throw new IllegalArgumentException("not right");
        }
        if(newROw!=0)
        getContext().getContentResolver().notifyChange(uri,null);
        return newROw;
    }
    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = petDbHelper.getWritableDatabase();
         int row_delete=0;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                // Delete all rows that match the selection and selection args
                row_delete= database.delete(petContract.Shelter.TABLE_NAME, selection, selectionArgs);
                break;
            case PETS_ID:
                // Delete a single row given by the ID in the URI
                selection = petContract.Shelter._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                 row_delete=database.delete(petContract.Shelter.TABLE_NAME, selection, selectionArgs);
                 break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if(row_delete!=0)
            getContext().getContentResolver().notifyChange(uri,null);

return row_delete;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return petContract.Shelter.CONTENT_LIST_TYPE;
            case PETS_ID:
                return petContract.Shelter .CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
