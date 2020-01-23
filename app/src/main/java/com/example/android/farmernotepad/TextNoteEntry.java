package com.example.android.farmernotepad;

public class TextNoteEntry {
    private int noteID;
    private String noteTitle;
    private String noteText;
    private String createDate;
    private String modDate;
    private int color;
    private String location;


    public TextNoteEntry(int noteID, String noteTitle, String noteText, String createDate, String modDate, int color, String location) {
        this.noteID = noteID;
        this.noteTitle = noteTitle;
        this.noteText = noteText;
        this.createDate = createDate;
        this.modDate = modDate;
        this.color = color;
        this.location = location;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
