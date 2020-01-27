package com.example.android.farmernotepad;

public class TextNoteEntry {
    private int noteID;
    private String noteTitle;
    private String noteText;
    private String createDate;
    private String modDate;
    private int color;
    private float latitude;
    private float longitude;




    public TextNoteEntry(int noteID, String noteTitle, String noteText, String createDate, String modDate, int color, float longitude, float latitude) {

        this.noteID = noteID;
        this.noteTitle = noteTitle;
        this.noteText = noteText;
        this.createDate = createDate;
        this.modDate = modDate;
        this.color = color;
        this.latitude = latitude;
        this.longitude = longitude;

    }


    public TextNoteEntry(){  //NULL Constructor for testing purposes
    }

    public int getNoteID() {
        return noteID;
    }

    public void setNoteID(int noteID) {
        this.noteID = noteID;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getModDate() {
        return modDate;
    }

    public void setModDate(String modDate) {
        this.modDate = modDate;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setLongtitude(float longitude) {this.longitude = longitude;}

    public void setLatitude(float latitude) {this.latitude = latitude;}

    public float getLongitude() {return longitude;}

    public float getLatitude() {return latitude;}



}
