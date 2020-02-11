package com.example.android.farmernotepad;

public interface ListItem {

    int typeText = 1;
    int typeChecklist = 2;

    int getListItemType();
    String getInterfaceTitle();
    String getInterfaceText();
    int getColor();
    String getModDate();
    String getCreateDate();

}