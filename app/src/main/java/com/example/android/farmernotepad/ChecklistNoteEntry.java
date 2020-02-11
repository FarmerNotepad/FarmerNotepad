package com.example.android.farmernotepad;

import java.util.ArrayList;

public class ChecklistNoteEntry implements ListItem{
    private int noteID;
    private String noteTitle;
    private String createDate;
    private String modDate;
    private int color;
    private double latitude;
    private double longitude;
    private ArrayList<String> checklistItems;

    public ChecklistNoteEntry(int noteID, String noteTitle, String createDate, String modDate, int color, double latitude, double longitude,ArrayList<String> items) {
        this.noteID = noteID;
        this.noteTitle = noteTitle;
        this.createDate = createDate;
        this.modDate = modDate;
        this.color = color;
        this.latitude = latitude;
        this.longitude = longitude;
        this.checklistItems = items;
    }

    public ChecklistNoteEntry() {}

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

    @Override
    public String getCreateDate() {
        return createDate;
    }


    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String getModDate() {
        return modDate;
    }

    public void setModDate(String modDate) {
        this.modDate = modDate;
    }

    @Override
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setChecklistItems(ArrayList<String> items) {this.checklistItems = items;}

    public ArrayList<String> getChecklistItems() {
        return checklistItems; }

    @Override
    public int getListItemType() {
        return ListItem.typeChecklist;
    }

    @Override
    public  String getInterfaceTitle(){
        return noteTitle;
    }

    @Override
    public String getInterfaceText(){
        return checklistItems.toString();
    }

}