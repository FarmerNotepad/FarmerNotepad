package com.example.android.farmernotepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    SQLiteDatabase dbRead,dbWrite;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FarmerNotepad.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FeedReaderContract.SQL_CREATE_TABLE_Text_Note);
        db.execSQL(FeedReaderContract.SQL_CREATE_TABLE_Checklist_Note);
        db.execSQL(FeedReaderContract.SQL_CREATE_TABLE_Checklist_Items);
        db.execSQL(FeedReaderContract.SQL_CREATE_TABLE_Employees);
        db.execSQL(FeedReaderContract.SQL_CREATE_TABLE_Wages);
        dbRead = getReadableDatabase();
        dbWrite = getWritableDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    public boolean insertNote(TextNoteEntry note){
        ContentValues cv = new ContentValues();
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteTitle,note.getNoteTitle());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteText,note.getNoteText());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteCreateDate,note.getCreateDate());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteModDate,note.getModDate());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_color,note.getColor());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteLatitude,note.getLatitude());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteLongitude,note.getLongitude());
        long rowInserted = dbWrite.insert(FeedReaderContract.FeedTextNote.TABLE_NAME_Text_Note, null ,cv);
        if (rowInserted != -1)
            return true;
        else
            return false;
    }

    public boolean updateNote(TextNoteEntry note){
        ContentValues cv = new ContentValues();
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteTitle,note.getNoteTitle());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteText,note.getNoteText());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteModDate,note.getModDate());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_color,note.getColor());
        long rowUpdated = dbWrite.update(FeedReaderContract.FeedTextNote.TABLE_NAME_Text_Note, cv,FeedReaderContract.FeedTextNote.COLUMN_ID + "=?", new String[]{String.valueOf(note.getNoteID())});
        if (rowUpdated != -1)
            return true;
        else
            return false;
    }

    Cursor getAllNotes(){
        return dbRead.rawQuery("SELECT * FROM "+ FeedReaderContract.FeedTextNote.TABLE_NAME_Text_Note, null);
    }

    Cursor getNote(int noteId){
        return  dbRead.rawQuery("SELECT * FROM " + FeedReaderContract.FeedTextNote.TABLE_NAME_Text_Note + " WHERE " + FeedReaderContract.FeedTextNote.COLUMN_ID + "=?", new String[]{String.valueOf(noteId)});
    }

    public boolean deleteNote(int noteID){
        long rowDeleted = dbWrite.delete(FeedReaderContract.FeedTextNote.TABLE_NAME_Text_Note, FeedReaderContract.FeedTextNote.COLUMN_ID+ "= ?", new String[] {String.valueOf(noteID)});
        if (rowDeleted > 0)
            return true;
        else
            return false;
    }

    public boolean insertChecklist(ChecklistNoteEntry checklist){
        ContentValues cv = new ContentValues();
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteTitle,checklist.getNoteTitle());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteCreateDate,checklist.getCreateDate());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteModDate,checklist.getModDate());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_color,checklist.getColor());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteLatitude,checklist.getLatitude());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteLongitude,checklist.getLongitude());
        long lastID = dbWrite.insert(FeedReaderContract.FeedTextNote.TABLE_NAME_Checklist_Note,null,cv);
        if (lastID != -1){
            int newID = (int) lastID;
            ContentValues cvitems = new ContentValues();
            ArrayList<String> items = checklist.getChecklistItems();
            if (items != null) {
            for (int i = 0; i < items.size() ; i++) {
                cvitems.put(FeedReaderContract.FeedTextNote.COLUMN_Item_note_Rel,newID);
                cvitems.put(FeedReaderContract.FeedTextNote.COLUMN_Item_Text,items.get(i));
                long checklistItemCheck =dbWrite.insert(FeedReaderContract.FeedTextNote.TABLE_NAME_Checklist_Items,null,cvitems);
                cvitems.clear();
                if (checklistItemCheck == -1) {
                    break;
                    }
                }
            }
            return true;
            } else {
            return false;
            }
        }

        public boolean deleteChecklist(int checklistID){
                long rowDeleted = dbWrite.delete(FeedReaderContract.FeedTextNote.TABLE_NAME_Checklist_Note, FeedReaderContract.FeedTextNote.COLUMN_ID+ "= ?", new String[] {String.valueOf(checklistID)});
                if (rowDeleted > 0)
                    return true;
                else
                    return false;
            }


          public Cursor getAllChecklists(){
              return dbRead.rawQuery("SELECT * FROM " + FeedReaderContract.FeedTextNote.TABLE_NAME_Checklist_Note, null);
          }

          public Cursor getChecklistItems(int checklistID){
              return dbRead.rawQuery("SELECT * FROM " + FeedReaderContract.FeedTextNote.TABLE_NAME_Checklist_Items
                      + " WHERE " + FeedReaderContract.FeedTextNote.COLUMN_Item_note_Rel + "=?",new String[]{String.valueOf(checklistID)});

          }

          public Cursor getSingleChecklist(int checklistID){
                return dbRead.rawQuery("SELECT * FROM " + FeedReaderContract.FeedTextNote.TABLE_NAME_Checklist_Note + " WHERE " +
                        FeedReaderContract.FeedTextNote.COLUMN_ID + " =?",new String[]{String.valueOf(checklistID)});
          }

          public Cursor getSingleChecklistItems(int checklistID){
                return dbRead.rawQuery("SELECT * FROM " + FeedReaderContract.FeedTextNote.TABLE_NAME_Checklist_Items + " WHERE " +
                        FeedReaderContract.FeedTextNote.COLUMN_Item_note_Rel + "=?",new String[]{String.valueOf(checklistID)});
          }

    public boolean updateChecklist(ChecklistNoteEntry checklist) {
        ContentValues cv = new ContentValues();
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteTitle, checklist.getNoteTitle());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteModDate, checklist.getModDate());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_color, checklist.getColor());
        long rowUpdated = dbWrite.update(FeedReaderContract.FeedTextNote.TABLE_NAME_Checklist_Note, cv, FeedReaderContract.FeedTextNote.COLUMN_ID + "=?", new String[]{String.valueOf(checklist.getNoteID())});
        long rowDeleteItems = dbWrite.delete(FeedReaderContract.FeedTextNote.TABLE_NAME_Checklist_Items,  FeedReaderContract.FeedTextNote.COLUMN_Item_note_Rel + "=?", new String[]{String.valueOf(checklist.getNoteID())});
        ContentValues cvItems = new ContentValues();
        for (int i = 0; i < checklist.getChecklistItems().size(); i++){
            cvItems.put(FeedReaderContract.FeedTextNote.COLUMN_Item_note_Rel,checklist.getNoteID());
            cvItems.put(FeedReaderContract.FeedTextNote.COLUMN_Item_Text,checklist.getChecklistItems().get(i));
            dbWrite.insert(FeedReaderContract.FeedTextNote.TABLE_NAME_Checklist_Items,null,cvItems);
        }
        if (rowUpdated != -1 && rowDeleteItems != 0)
            return true;
        else
            return false;
    }

    public boolean insertEmployee(Employee employee){
        ContentValues cv = new ContentValues();
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_emp_Name,employee.getEmployeeName());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_emp_Phone,employee.getEmployeePhone());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_emp_Sum,employee.getEmployeeSum());
        long checkInsert = dbWrite.insert(FeedReaderContract.FeedTextNote.TABLE_NAME_Employees,null,cv);
        if (checkInsert != -1)
            return true;
        else
            return false;
    }

    Cursor getAllEmployees(){
        return dbRead.rawQuery("SELECT * FROM "+ FeedReaderContract.FeedTextNote.TABLE_NAME_Employees, null);
    }

    Cursor getEmployee(int empID){
        return  dbRead.rawQuery("SELECT * FROM " + FeedReaderContract.FeedTextNote.TABLE_NAME_Employees + " WHERE " + FeedReaderContract.FeedTextNote.COLUMN_ID + "=?", new String[]{String.valueOf(empID)});
    }

    public boolean deleteEmployee(int empID){
        long rowDeleted = dbWrite.delete(FeedReaderContract.FeedTextNote.TABLE_NAME_Text_Note, FeedReaderContract.FeedTextNote.COLUMN_ID+ "= ?", new String[] {String.valueOf(empID)});
        if (rowDeleted > 0)
            return true;
        else
            return false;
    }



}





