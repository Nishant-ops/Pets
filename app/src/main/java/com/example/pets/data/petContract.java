package com.example.pets.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class petContract {
private petContract(){};
    public final static String CONTENT_AUTHORITY="com.example.android.pets";
    public final static Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);
    public final static String PATH_PETS="pets";
    public static class Shelter implements BaseColumns
    {
        public final static Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_PETS);
         public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_FEMALE =2;
        public static final int GENDER_MALE = 1;
        public static final String TABLE_NAME = "pets";
        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_PET_NAME="name";
        public final static String COLUMN_PET_BREED="breed";
        public final static String COLUMN_PET_GENDER="gender";
        public final static String COLUMN_PET_Weight="weight";
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;



    }
}
