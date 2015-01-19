package com.crixmod.sailorcast.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by fire3 on 2015/1/14.
 */
public class BookmarkDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "BookmarkManager";
    private static final String TABLE_BOOKMARK = "bookmarks";
    private static final String KEY_ID = "id";
    private static final String KEY_ALBUM_JSON = "albumJson";
    private static final String KEY_ALBUM_ID = "albumID";
    private static final String KEY_ALBUM_SITE = "albumSite";
    private static final String KEY_CREATED_AT = "createdAt";
    private static final String LOG_TAG = "SailorCast:BookmarkDbHelper";

    public BookmarkDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_BOOKMARK + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ALBUM_ID + " TEXT,"
                + KEY_ALBUM_SITE + " INTEGER,"
                + KEY_ALBUM_JSON + " TEXT,"
                + KEY_CREATED_AT + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARK);
        // Create tables again
        onCreate(db);
    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARK);
        // Create tables again
        onCreate(db);
    }


    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void addAlbum(SCAlbum album) {

        SCAlbum old = getAlbumById(album.getAlbumId(), album.getSite().getSiteID());
        SQLiteDatabase db = this.getWritableDatabase();

        if (old == null) {
            ContentValues values = new ContentValues();
            values.put(KEY_ALBUM_ID, album.getAlbumId());
            values.put(KEY_ALBUM_SITE, album.getSite().getSiteID());
            values.put(KEY_ALBUM_JSON, album.toJson());
            values.put(KEY_CREATED_AT, getDateTime());
            // Inserting Row
            db.insert(TABLE_BOOKMARK, null, values);
            db.close(); // Closing database connection
        }

    }

    public SCAlbum getAlbumById(String albumId, int siteID) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKMARK, new String[]{
                        KEY_ALBUM_ID, KEY_ALBUM_SITE,
                        KEY_ALBUM_JSON}, KEY_ALBUM_ID + "=? AND " + KEY_ALBUM_SITE + "=?",
                new String[]{String.valueOf(albumId), String.valueOf(siteID)}, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst() == true) {
                String json = cursor.getString(2);
                SCAlbum album = SCAlbum.fromJson(json);
                cursor.close();
                db.close();
                return album;
            } else {
                cursor.close();
                db.close();
                return null;
            }
        } else {
            db.close();
        }
        return null;
    }

    public void deleteAlbum(String albumID, int siteID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKMARK, KEY_ALBUM_ID + " = ? AND " + KEY_ALBUM_SITE + " = ?",
                new String[]{String.valueOf(albumID), String.valueOf(siteID)});
        db.close();
    }

    public int getTotalCount() {
        String countQuery = "SELECT * FROM " + TABLE_BOOKMARK;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }

    public SCAlbums getAllAlbums() {
        SCAlbums albums = new SCAlbums();
        try {
            // Select All Query
            String selectQuery = "SELECT * FROM " + TABLE_BOOKMARK + " ORDER BY datetime(" + KEY_CREATED_AT + ") DESC";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    SCAlbum album = SCAlbum.fromJson(cursor.getString(3));
                    albums.add(album);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return albums;
        } catch (Exception e) {
            Log.e(LOG_TAG, "" + e);
        }
        return null;
    }

    public SCAlbums getAlbumsByPage(int PageNo, int PageSize) {
         SCAlbums albums = new SCAlbums();
        try {
            String selectQuery = "SELECT * FROM " + TABLE_BOOKMARK +
                    " Limit "+String.valueOf(PageSize)+ " Offset " +String.valueOf(PageNo*PageSize) +
                    " ORDER BY datetime(" + KEY_CREATED_AT + ") DESC";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    SCAlbum album = SCAlbum.fromJson(cursor.getString(3));
                    albums.add(album);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return albums;
        } catch (Exception e) {
            Log.e(LOG_TAG, "" + e);
        }
        return null;
    }



}
