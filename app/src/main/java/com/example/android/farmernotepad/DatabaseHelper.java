package com.example.android.farmernotepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {


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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    public boolean insertNote(EntryTextNote note) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteTitle, note.getNoteTitle());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteText, note.getNoteText());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteCreateDate, note.getCreateDate());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteModDate, note.getModDate());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_color, note.getColor());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteLatitude, note.getLatitude());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteLongitude, note.getLongitude());
        long rowInserted = db.insert(FeedReaderContract.FeedTextNote.TABLE_NAME_Text_Note, null, cv);
        if (rowInserted != -1)
            return true;
        else
            return false;
    }

    public boolean updateNote(EntryTextNote note) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteTitle, note.getNoteTitle());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteText, note.getNoteText());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteModDate, note.getModDate());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_color, note.getColor());
        long rowUpdated = db.update(FeedReaderContract.FeedTextNote.TABLE_NAME_Text_Note, cv, FeedReaderContract.FeedTextNote.COLUMN_ID + "=?", new String[]{String.valueOf(note.getNoteID())});
        if (rowUpdated != -1)
            return true;
        else
            return false;
    }

    Cursor getAllNotes() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + FeedReaderContract.FeedTextNote.TABLE_NAME_Text_Note, null);
    }

    Cursor getNote(int noteId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + FeedReaderContract.FeedTextNote.TABLE_NAME_Text_Note + " WHERE " + FeedReaderContract.FeedTextNote.COLUMN_ID + "=?", new String[]{String.valueOf(noteId)});
    }

    public boolean deleteNote(int noteID) {
        SQLiteDatabase db = getWritableDatabase();
        long rowDeleted = db.delete(FeedReaderContract.FeedTextNote.TABLE_NAME_Text_Note, FeedReaderContract.FeedTextNote.COLUMN_ID + "= ?", new String[]{String.valueOf(noteID)});
        if (rowDeleted > 0)
            return true;
        else
            return false;
    }

    public boolean insertChecklist(EntryChecklistNote checklist) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteTitle, checklist.getNoteTitle());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteCreateDate, checklist.getCreateDate());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteModDate, checklist.getModDate());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_color, checklist.getColor());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteLatitude, checklist.getLatitude());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteLongitude, checklist.getLongitude());
        long lastID = db.insert(FeedReaderContract.FeedTextNote.TABLE_NAME_Checklist_Note, null, cv);
        if (lastID != -1) {
            int newID = (int) lastID;
            ContentValues cvitems = new ContentValues();
            ArrayList<String> items = checklist.getChecklistItems();
            if (items != null) {
                for (int i = 0; i < items.size(); i++) {
                    cvitems.put(FeedReaderContract.FeedTextNote.COLUMN_Item_note_Rel, newID);
                    cvitems.put(FeedReaderContract.FeedTextNote.COLUMN_Item_Text, items.get(i));
                    long checklistItemCheck = db.insert(FeedReaderContract.FeedTextNote.TABLE_NAME_Checklist_Items, null, cvitems);
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

    public boolean deleteChecklist(int checklistID) {
        SQLiteDatabase db = getWritableDatabase();
        long rowDeleted = db.delete(FeedReaderContract.FeedTextNote.TABLE_NAME_Checklist_Note, FeedReaderContract.FeedTextNote.COLUMN_ID + "= ?", new String[]{String.valueOf(checklistID)});
        if (rowDeleted > 0)
            return true;
        else
            return false;
    }


    public Cursor getAllChecklists() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + FeedReaderContract.FeedTextNote.TABLE_NAME_Checklist_Note, null);
    }

    public Cursor getChecklistItems(int checklistID) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + FeedReaderContract.FeedTextNote.TABLE_NAME_Checklist_Items
                + " WHERE " + FeedReaderContract.FeedTextNote.COLUMN_Item_note_Rel + "=?", new String[]{String.valueOf(checklistID)});

    }

    public Cursor getSingleChecklist(int checklistID) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + FeedReaderContract.FeedTextNote.TABLE_NAME_Checklist_Note + " WHERE " +
                FeedReaderContract.FeedTextNote.COLUMN_ID + " =?", new String[]{String.valueOf(checklistID)});
    }

    public Cursor getSingleChecklistItems(int checklistID) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + FeedReaderContract.FeedTextNote.TABLE_NAME_Checklist_Items + " WHERE " +
                FeedReaderContract.FeedTextNote.COLUMN_Item_note_Rel + "=?", new String[]{String.valueOf(checklistID)});
    }

    public boolean updateChecklist(EntryChecklistNote checklist) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteTitle, checklist.getNoteTitle());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_noteModDate, checklist.getModDate());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_color, checklist.getColor());
        long rowUpdated = db.update(FeedReaderContract.FeedTextNote.TABLE_NAME_Checklist_Note, cv, FeedReaderContract.FeedTextNote.COLUMN_ID + "=?", new String[]{String.valueOf(checklist.getNoteID())});
        long rowDeleteItems = db.delete(FeedReaderContract.FeedTextNote.TABLE_NAME_Checklist_Items, FeedReaderContract.FeedTextNote.COLUMN_Item_note_Rel + "=?", new String[]{String.valueOf(checklist.getNoteID())});
        ContentValues cvItems = new ContentValues();
        for (int i = 0; i < checklist.getChecklistItems().size(); i++) {
            cvItems.put(FeedReaderContract.FeedTextNote.COLUMN_Item_note_Rel, checklist.getNoteID());
            cvItems.put(FeedReaderContract.FeedTextNote.COLUMN_Item_Text, checklist.getChecklistItems().get(i));
            db.insert(FeedReaderContract.FeedTextNote.TABLE_NAME_Checklist_Items, null, cvItems);
        }
        if (rowUpdated != -1 && rowDeleteItems != 0)
            return true;
        else
            return false;
    }

    public boolean insertEmployee(EntryEmployee entryEmployee) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_emp_Name, entryEmployee.getEmployeeName());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_emp_Phone, entryEmployee.getEmployeePhone());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_emp_Sum, entryEmployee.getEmployeeSum());
        long lastID = db.insert(FeedReaderContract.FeedTextNote.TABLE_NAME_Employees, null, cv);
        if (lastID != -1) {
            int newID = (int) lastID;
            ContentValues cvitems = new ContentValues();
            ArrayList<EntryWage> wage = entryEmployee.getEmployeePaymentItems();
            if (wage != null) {
                for (int i = 0; i < wage.size(); i++) {
                    cvitems.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Rel, newID);
                    cvitems.put(FeedReaderContract.FeedTextNote.COLUMN_wage_CreateDate, wage.get(i).getWageCreateDate());
                    cvitems.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Desc, wage.get(i).getWageDesc());
                    cvitems.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Date, wage.get(i).getWageWorkDate());
                    cvitems.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Hours, wage.get(i).getWageHours());
                    cvitems.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Wage, wage.get(i).getWageWage());
                    cvitems.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Type, wage.get(i).getWageType());
                    long rowInserted = db.insert(FeedReaderContract.FeedTextNote.TABLE_NAME_Wages, null, cvitems);
                    cvitems.clear();

                    if (rowInserted == -1) {
                        break;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean updateEmployee(EntryEmployee entryEmployee) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_emp_Name, entryEmployee.getEmployeeName());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_emp_Phone, entryEmployee.getEmployeePhone());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_emp_Sum, entryEmployee.getEmployeeSum());
        long rowUpdated = db.update(FeedReaderContract.FeedTextNote.TABLE_NAME_Employees, cv, FeedReaderContract.FeedTextNote.COLUMN_ID + "=?", new String[]{String.valueOf(entryEmployee.getEmployeeID())});
        long rowDeleteItems = db.delete(FeedReaderContract.FeedTextNote.TABLE_NAME_Wages, FeedReaderContract.FeedTextNote.COLUMN_wage_Rel + "= ?", new String[]{String.valueOf(entryEmployee.getEmployeeID())});
        ContentValues cvItems = new ContentValues();
        ArrayList<EntryWage> wage = entryEmployee.getEmployeePaymentItems();

        for (int i = 0; i < wage.size(); i++) {
            cvItems.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Rel, entryEmployee.getEmployeeID());
            cvItems.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Desc, wage.get(i).getWageDesc());
            cvItems.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Date, wage.get(i).getWageWorkDate());
            cvItems.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Hours, wage.get(i).getWageHours());
            cvItems.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Wage, wage.get(i).getWageWage());
            cvItems.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Type, wage.get(i).getWageType());
            db.insert(FeedReaderContract.FeedTextNote.TABLE_NAME_Wages, null, cvItems);
        }

        if (rowUpdated != -1 && rowDeleteItems != 0)
            return true;
        else
            return false;
    }

    Cursor getAllEmployees() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + FeedReaderContract.FeedTextNote.TABLE_NAME_Employees, null);
    }

    Cursor getEmployee(int empID) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + FeedReaderContract.FeedTextNote.TABLE_NAME_Employees + " WHERE " + FeedReaderContract.FeedTextNote.COLUMN_ID + "=?", new String[]{String.valueOf(empID)});
    }

    public boolean deleteEmployee(int empID) {
        SQLiteDatabase db = getWritableDatabase();
        long rowDeleted = db.delete(FeedReaderContract.FeedTextNote.TABLE_NAME_Employees, FeedReaderContract.FeedTextNote.COLUMN_ID + "= ?", new String[]{String.valueOf(empID)});
        if (rowDeleted > 0)
            return true;
        else
            return false;
    }

    public boolean insertWage(EntryWage wage, int relID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Rel, relID);
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_wage_CreateDate, wage.getWageCreateDate());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Desc, wage.getWageDesc());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Date, wage.getWageWorkDate());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Hours, wage.getWageHours());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Wage, wage.getWageWage());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Type, wage.getWageType());
        long rowInserted = db.insert(FeedReaderContract.FeedTextNote.TABLE_NAME_Wages, null, cv);
        if (rowInserted != -1)
            return true;
        else
            return false;
    }

    public boolean deleteWage(int wageID) {
        SQLiteDatabase db = getWritableDatabase();
        long rowDeleted = db.delete(FeedReaderContract.FeedTextNote.TABLE_NAME_Wages, FeedReaderContract.FeedTextNote.COLUMN_ID + "= ?", new String[]{String.valueOf(wageID)});
        if (rowDeleted > 0)
            return true;
        else
            return false;
    }

    public boolean updateWage(EntryWage wage) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Desc, wage.getWageDesc());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Date, wage.getWageWorkDate());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Hours, wage.getWageHours());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Wage, wage.getWageWage());
        cv.put(FeedReaderContract.FeedTextNote.COLUMN_wage_Type, wage.getWageType());
        long checkUpdate = db.update(FeedReaderContract.FeedTextNote.TABLE_NAME_Wages, cv, FeedReaderContract.FeedTextNote.COLUMN_ID + "=?", new String[]{String.valueOf(wage.getWageID())});
        if (checkUpdate != -1)
            return true;
        else
            return false;
    }

    Cursor getEmployeeWages(int empID) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + FeedReaderContract.FeedTextNote.TABLE_NAME_Wages + " WHERE "
                + FeedReaderContract.FeedTextNote.COLUMN_wage_Rel + "=?", new String[]{String.valueOf(empID)});
    }

        public void exportDB (Context ctx){
            try {
                File dbFile = new File(ctx.getDatabasePath(DATABASE_NAME).getAbsolutePath());
                FileInputStream fis = new FileInputStream(dbFile);

                String outFileName = ctx.getExternalFilesDir(null) + File.separator +
                        DATABASE_NAME;
                File outFile = new File(outFileName);
                if (outFile.exists()) {
                    outFile.delete();
                    outFile = new File(outFileName);
                }

                // Open the empty db as the output stream
                OutputStream output = new FileOutputStream(outFile);

                // Transfer bytes from the inputfile to the outputfile
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                // Close the streams
                output.flush();
                output.close();
                fis.close();


            } catch (IOException e) {
                Log.e("dbBackup:", e.getMessage());
            }
        }


}








