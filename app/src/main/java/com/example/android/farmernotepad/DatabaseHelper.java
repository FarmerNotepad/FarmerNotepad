package com.example.android.farmernotepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    //public String testSQL = "INSERT INTO " + FeedReaderContract.FeedTextNote.TABLE_NAME_Text_Note +
     //       "(" + FeedReaderContract.FeedTextNote.COLUMN_NAME_noteText +
     //       ") VALUES ('PAPALA');";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FarmerNotepad.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FeedReaderContract.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public boolean insertNote(TextNoteEntry note){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteTitle,note.getNoteTitle());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteText,note.getNoteText());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteCreateDate,note.getCreateDate());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteModDate,note.getModDate());
        long rowInserted = db.insert(FeedReaderContract.FeedTextNote.TABLE_NAME_Text_Note, null ,cv);
        if (rowInserted != -1)
            return true;
        else
            return false;
    }

    Cursor getAllNotes(){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM "+ FeedReaderContract.FeedTextNote.TABLE_NAME_Text_Note, null);
    }

}