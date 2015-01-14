package com.crixmod.sailorcast.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fire3 on 2015/1/14.
 */
public class BookmarkDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_NAME = "BookmarkManager";
    private static final String TABLE_BOOKMARK = "bookmarks";
    private static final String KEY_ID = "id";
    private static final String KEY_ALBUM_JSON = "albumID";
    private static final String KEY_CREATED_AT = "createdAt";

    public BookmarkDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
  String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_BOOKMARK + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ALBUM_JSON + " TEXT,"
                + KEY_CREATED_AT + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
