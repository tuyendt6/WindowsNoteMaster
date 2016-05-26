package com.kidlandstudio.stikkesnote.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.kidlandstudio.stikkesnote.db.table.tblNote;


/**
 * Created by tuyenpx on 27/04/2016.
 */
public class DatabaseManager extends ContentProvider {
    public static final String PRO_NAME = "com.rikkie.noteapp.db";

    // 0
    public static final Uri URI_NOTE = Uri.parse("content://" + PRO_NAME + "/"
            + tblNote.TBL_NAME);

    private static final int M_NOTE = 0;
    public static final UriMatcher uriMatcher;

    private static final String TAG = DatabaseManager.class.getSimpleName();

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PRO_NAME, tblNote.TBL_NAME, M_NOTE);
    }

    private static final String DATABASE_NAME = "Database_SmallNote";

    private static final int DATABASE_VERSION = 6;

    private DatabaseHelper mDbHelper;


    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            tblNote.onCreate(db); //0
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            tblNote.onUpgrade(db, oldVersion, newVersion); //0
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String table = "";
        switch (uriMatcher.match(uri)) {
            case M_NOTE: //0
                table = tblNote.TBL_NAME;
                break;
            default:
                break;
        }
        Log.e(TAG, "Xoa table " + table);

        SQLiteDatabase sqlDB = mDbHelper.getWritableDatabase();
        int deleteCount = sqlDB.delete(table, selection, selectionArgs);
        Log.e(TAG, "Tong so dong da xoa = " + deleteCount);
        // Thong bao den cac observer ve su thay doi
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        int key = uriMatcher.match(uri);
        String table = "";

        switch (key) {
            case M_NOTE: //0
                table = tblNote.TBL_NAME;
                break;
            default:
                break;
        }


        SQLiteDatabase sqlDB = mDbHelper.getWritableDatabase();
        long rowID = sqlDB.insertWithOnConflict(table, "", values, SQLiteDatabase.CONFLICT_REPLACE);
        getContext().getContentResolver().notifyChange(uri, null);
        if (rowID > 0) {
            return Uri.withAppendedPath(uri, String.valueOf(rowID));
        } else {
            return null;

        }
    }


    @Override
    public boolean onCreate() {

        Context context = getContext();
        mDbHelper = new DatabaseHelper(context);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        int key = uriMatcher.match(uri);
        String table = "";

        switch (key) {
            case M_NOTE://0
                table = tblNote.TBL_NAME;
                break;
            default:
                break;
        }

        SQLiteDatabase sqlDB = mDbHelper.getWritableDatabase();
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        sqlBuilder.setTables(table);
        sqlBuilder.setDistinct(true);
        Cursor c = sqlBuilder.query(sqlDB, projection, selection, selectionArgs, null, null,
                sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int key = uriMatcher.match(uri);
        String table = "";

        switch (key) {
            case M_NOTE://0
                table = tblNote.TBL_NAME;
                break;
            default:
                break;
        }
        SQLiteDatabase sqlDB = mDbHelper.getWritableDatabase();
        int rowEffect = sqlDB.update(table, values, selection, selectionArgs);
        if (rowEffect > 0) {
        }
        return rowEffect;
    }
}
