package com.example.android.farmernotepad;

public class FeedReaderContract {
    private FeedReaderContract(){}

    public static class FeedTextNote {
        public final static String TABLE_NAME_Text_Note = "TextNote";
        public final static String TABLE_NAME_Checklist_Note = "Checklist";
        public final static String TABLE_NAME_Checklist_Items = "ChecklistItems";
        public final static String TABLE_NAME_Employees = "Employees";
        public final static String TABLE_NAME_Wages = "Wages";
        public final static String COLUMN_ID = "ID";
        public final static String COLUMN_noteTitle = "noteTitle";
        public final static String COLUMN_noteText = "noteText";
        public final static String COLUMN_color = "noteColor";
        public final static String COLUMN_noteLongitude = "noteLongitude";
        public final static String COLUMN_noteLatitude = "noteLatitude";
        public final static String COLUMN_noteCreateDate = "noteCreateDate";
        public final static String COLUMN_noteModDate = "noteModDate";
        public final static String COLUMN_Item_ID = "itemID";
        public final static String COLUMN_Item_Text = "itemText";
        public final static String COLUMN_Item_note_Rel = "itemNoteID";
        public final static String COLUMN_emp_Name = "EmployeeName";
        public final static String COLUMN_emp_Phone = "EmployeePhone";
        public final static String COLUMN_emp_Sum = "EmployeeSum";
        public final static String COLUMN_wage_Rel = "EmployeeID";
        public final static String COLUMN_wage_Rate = "wageRate";
        public final static String COLUMN_wage_Desc = "wageDesc";
        public final static String COLUMN_wage_Hours = "workHours";
        public final static String COLUMN_wage_Date = "dateOfWork";
        public final static String COLUMN_wage_Type = "wageType";
        public final static String COLUMN_wage_CreateDate = "wageCreateDate";

    }
    public static final String SQL_CREATE_TABLE_Text_Note =
            "CREATE TABLE IF NOT EXISTS " + FeedTextNote.TABLE_NAME_Text_Note + "(" + FeedTextNote.COLUMN_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT," +FeedTextNote.COLUMN_noteTitle + " TEXT," +
                    FeedTextNote.COLUMN_noteText + " TEXT," + FeedTextNote.COLUMN_color
                    + " INTEGER," +FeedTextNote.COLUMN_noteLatitude + " REAL," + FeedTextNote.COLUMN_noteLongitude +
                    " REAL," + FeedTextNote.COLUMN_noteCreateDate + " TEXT," +
                    FeedTextNote.COLUMN_noteModDate + " TEXT);" ;

    public static final String SQL_CREATE_TABLE_Checklist_Note =
            "CREATE TABLE IF NOT EXISTS " + FeedTextNote.TABLE_NAME_Checklist_Note + "(" + FeedTextNote.COLUMN_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT," +FeedTextNote.COLUMN_noteTitle + " TEXT," +
                      FeedTextNote.COLUMN_color
                    + " INTEGER," +FeedTextNote.COLUMN_noteLatitude + " REAL," + FeedTextNote.COLUMN_noteLongitude +
                    " REAL," + FeedTextNote.COLUMN_noteCreateDate + " TEXT," +
                    FeedTextNote.COLUMN_noteModDate + " TEXT);" ;

    public static final String SQL_CREATE_TABLE_Checklist_Items =
            "CREATE TABLE IF NOT EXISTS " + FeedTextNote.TABLE_NAME_Checklist_Items + "(" + FeedTextNote.COLUMN_Item_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT," + FeedTextNote.COLUMN_Item_Text + " TEXT," + FeedTextNote.COLUMN_Item_note_Rel + " INTEGER," +
                    "FOREIGN KEY (" + FeedTextNote.COLUMN_Item_note_Rel + ") REFERENCES " +FeedTextNote.TABLE_NAME_Checklist_Note +"(" +
                    FeedTextNote.COLUMN_ID +") ON DELETE CASCADE);";

    public static final String SQL_CREATE_TABLE_Employees = "CREATE TABLE IF NOT EXISTS " +FeedTextNote.TABLE_NAME_Employees + "(" +FeedTextNote.COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT," + FeedTextNote.COLUMN_emp_Name + " TEXT," +FeedTextNote.COLUMN_emp_Phone + " INTEGER,"
            + FeedTextNote.COLUMN_emp_Sum + " REAL);";

    public static final String SQL_CREATE_TABLE_Wages = "CREATE TABLE IF NOT EXISTS " +FeedTextNote.TABLE_NAME_Wages + "(" +FeedTextNote.COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT," + FeedTextNote.COLUMN_wage_Desc + " TEXT," +FeedTextNote.COLUMN_wage_Rate + " REAL,"
            + FeedTextNote.COLUMN_wage_Hours + " REAL," + FeedTextNote.COLUMN_wage_Date + " TEXT," + FeedTextNote.COLUMN_wage_CreateDate + " TEXT," +
            FeedTextNote.COLUMN_wage_Type + " INTEGER," + FeedTextNote.COLUMN_wage_Rel + " INTEGER," +
            "FOREIGN KEY (" + FeedTextNote.COLUMN_wage_Rel + ") REFERENCES " + FeedTextNote.TABLE_NAME_Employees + "(" +
            FeedTextNote.COLUMN_ID + ") ON DELETE CASCADE);";


    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedTextNote.TABLE_NAME_Text_Note;

}


