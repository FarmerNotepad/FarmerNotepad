package com.example.android.farmernotepad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FeedReaderContract {
    private FeedReaderContract(){}

    public static class FeedTextNote {
        public final static String TABLE_NAME_Text_Note = "Text Note";
        public final static String COLUMN_NAME_ID = "noteID";
        public final static String COLUMN_NAME_noteTitle = "noteTitle";
        public final static String COLUMN_NAME_noteText = "noteText";
        public final static String COLUMN_NAME_color = "noteColor";
        public final static String COLUMN_NAME_noteLocation = "noteLocation";
        public final static String COLUMN_NAME_noteCreateDate = "noteCreateDate";
        public final static String COLUMN_NAME_noteModDate = "noteModDate";
    }
    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + FeedTextNote.TABLE_NAME_Text_Note + "(" + FeedTextNote.COLUMN_NAME_ID +
                    "INTEGER PRIMARY KEY ," +FeedTextNote.COLUMN_NAME_noteTitle + "TEXT," +
                    FeedTextNote.COLUMN_NAME_noteText + "TEXT," + FeedTextNote.COLUMN_NAME_color
                    + "INTEGER," +FeedTextNote.COLUMN_NAME_noteLocation + "REAL," +
                    FeedTextNote.COLUMN_NAME_noteCreateDate + "TEXT," +
                    FeedTextNote.COLUMN_NAME_noteModDate + "TEXT)" ;
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedTextNote.TABLE_NAME_Text_Note;

}


