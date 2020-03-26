package com.example.android.farmernotepad;

public interface ListItem {

    int typeText = 1;
    int typeChecklist = 2;

    int getListItemType();

    int getNoteID();

    String getInterfaceTitle();

    String getInterfaceText();

    int getColor();

    String getModDate();

    String getCreateDate();

    double getLatitude();

    double getLongitude();

}
