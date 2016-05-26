package com.kidlandstudio.stikkesnote.db.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by tuyenpx on 27/04/2016.
 */
public class tblNote {
    // Database table
    public static final String TBL_NAME = "tbl_Note";

    public static final String _ID = "_id";//1

    // public Note(String titleNote, String contentNote, int colorNote, String lastTimeModifyNote, long alarmNote, boolean isSetAlarm) {

    public static final String TITLE_NOTE = "title_note";//2

    public static final String CONTENT_NOTE = "content_note";//3

    public static final String COLOR_NOTE = "color_note";//4

    public static final String LASTTIME_MODIFY_NOTE = "last_time_modify_note";//5

    public static final String ALARM_NOTE = "alarm_note";//6

    public static final String IMAGE_NOTE = "image_note";//7
    public static final String ALARM_TIME_STRING = "alarm_time_string";//7

    public static final String IS_SET_ALARM = "is_set_alarm";//8

    private static String createData() {

        StringBuilder sBuiler = new StringBuilder();

        sBuiler.append("create table " + TBL_NAME + " (");
        sBuiler.append(_ID + " integer primary key autoincrement, ");//1
        sBuiler.append(TITLE_NOTE + " text, ");//2
        sBuiler.append(CONTENT_NOTE + " text, ");//3
        sBuiler.append(COLOR_NOTE + " integer, ");//4
        sBuiler.append(LASTTIME_MODIFY_NOTE + " text, ");//5
        sBuiler.append(ALARM_NOTE + " long, ");//6
        sBuiler.append(IMAGE_NOTE + " text, ");//8
        sBuiler.append(ALARM_TIME_STRING + " text, ");//7
        sBuiler.append(IS_SET_ALARM + " integer); ");//9
        return sBuiler.toString();
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(createData());
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TBL_NAME);
        onCreate(database);
    }

}
