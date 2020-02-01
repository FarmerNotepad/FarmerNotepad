package com.example.android.farmernotepad;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GenericUtils {

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
