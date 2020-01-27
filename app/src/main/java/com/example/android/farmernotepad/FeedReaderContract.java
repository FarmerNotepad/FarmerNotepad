package com.example.android.farmernotepad;

public class FeedReaderContract {
    private FeedReaderContract(){}

    public static class FeedTextNote {
        public final static String TABLE_NAME_Text_Note = "TextNote";
        public final static String COLUMN_ID = "noteID";
        public final static String COLUMN_noteTitle = "noteTitle";
        public final static String COLUMN_noteText = "noteText";
        public final static String COLUMN_color = "noteColor";
        public final static String COLUMN_noteLongitude = "noteLongitude";
        public final static String COLUMN_noteAltitude = "noteAltitude";
        public final static String COLUMN_noteCreateDate = "noteCreateDate";
        public final static String COLUMN_noteModDate = "noteModDate";
    }
    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + FeedTextNote.TABLE_NAME_Text_Note + "(" + FeedTextNote.COLUMN_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT," +FeedTextNote.COLUMN_noteTitle + " TEXT," +
                    FeedTextNote.COLUMN_noteText + " TEXT," + FeedTextNote.COLUMN_color
                    + " INTEGER," +FeedTextNote.COLUMN_noteLongitude + " REAL," + FeedTextNote.COLUMN_noteAltitude +
                    " REAL," + FeedTextNote.COLUMN_noteCreateDate + " TEXT," +
                    FeedTextNote.COLUMN_noteModDate + " TEXT);" ;
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedTextNote.TABLE_NAME_Text_Note;

}


