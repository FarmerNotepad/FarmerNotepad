package com.example.android.farmernotepad;

public class TextNoteEntry implements  ListItem{
    private int noteID;
    private String noteTitle;
    private String noteText;
    private String createDate;
    private String modDate;
    private int color;
    private double latitude;
    private double longitude;




    public TextNoteEntry(int noteID, String noteTitle, String noteText, String createDate, String modDate, int color, double longitude, double latitude) {

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

    public void setLongitude(double longitude) {this.longitude = longitude;}

    public void setLatitude(double latitude) {this.latitude = latitude;}

    public double getLongitude() {return longitude;}

    public double getLatitude() {return latitude;}


    @Override
    public int getListItemType() {
        return ListItem.typeText;
    }

    @Override
    public  String getInterfaceTitle(){
        return noteTitle;
    }
    @Override
    public String getInterfaceText(){
        return noteText;
}

}
