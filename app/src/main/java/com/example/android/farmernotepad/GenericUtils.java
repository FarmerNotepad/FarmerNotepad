package com.example.android.farmernotepad;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class GenericUtils {

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static ArrayList<ListItem> sortByTitle( ArrayList<ListItem> myList, final boolean desc){
            Collections.sort(myList, new Comparator<ListItem>() {

                @Override
                public int compare(ListItem note1, ListItem note2) {
                    if (!desc) {
                        return note1.getInterfaceTitle().toUpperCase().compareTo(note2.getInterfaceTitle().toUpperCase());
                    }
                    else {
                        return note2.getInterfaceTitle().toUpperCase().compareTo(note1.getInterfaceTitle().toUpperCase());
                    }
                }

            });
            return myList;
        }

    public static ArrayList<ListItem> sortByCreateDate( ArrayList<ListItem> myList, final boolean desc) {
        Collections.sort(myList, new Comparator<ListItem>() {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date date1, date2;

            @Override
            public int compare(ListItem note1, ListItem note2) {
                try {
                    date1 = df.parse(note1.getCreateDate());
                    date2 = df.parse(note2.getCreateDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                    if (!desc) {
                        return date1.compareTo(date2);
                    } else {
                        return date2.compareTo(date1);
                    }
            }
        });
        return myList;
    }

    public static ArrayList<ListItem> sortByModDate( ArrayList<ListItem> myList, final boolean desc) {
        Collections.sort(myList, new Comparator<ListItem>() {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date date1, date2;

            @Override
            public int compare(ListItem note1, ListItem note2) {
                try {
                    date1 = df.parse(note1.getModDate());
                    date2 = df.parse(note2.getModDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (!desc) {
                    return date1.compareTo(date2);
                } else {
                    return date2.compareTo(date1);
                }
            }
        });
        return myList;
    }

    public static ArrayList<ListItem> sortByColor(ArrayList<ListItem> myList, final boolean desc) {
        Collections.sort(myList, new Comparator<ListItem>() {

            @Override
            public int compare(ListItem note1, ListItem note2) {

                if (!desc) {
                    return Integer.compare(note1.getColor(),note2.getColor());
                } else {
                    return Integer.compare(note2.getColor(),note1.getColor());
                }
            }
        });
        return myList;
    }

}
