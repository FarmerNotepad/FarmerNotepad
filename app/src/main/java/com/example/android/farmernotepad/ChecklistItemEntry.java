package com.example.android.farmernotepad;

public class ChecklistItemEntry {
    private int itemID;
    private int itemNoteID;
    private String itemText;

    public ChecklistItemEntry(int itemID, int itemNoteID, String itemText) {
        this.itemID = itemID;
        this.itemNoteID = itemNoteID;
        this.itemText = itemText;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getItemNoteID() {
        return itemNoteID;
    }

    public void setItemNoteID(int itemNoteID) {
        this.itemNoteID = itemNoteID;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }
}
