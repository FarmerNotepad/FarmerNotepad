package com.example.android.farmernotepad;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.core.graphics.drawable.DrawableCompat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static androidx.core.content.ContextCompat.getSystemService;

public class GenericUtils {
    public static final String CHANNEL_ID = "channel1";

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static Date addMonth(Date dateToIncrease){
        Calendar myCal = Calendar.getInstance();
        myCal.setTime(dateToIncrease);
        myCal.add(Calendar.MONTH, +1);
        dateToIncrease = myCal.getTime();
        return dateToIncrease;
    }

    public static ArrayList<ListItem> sortByTitle(ArrayList<ListItem> myList, final boolean desc) {
        Collections.sort(myList, new Comparator<ListItem>() {

            @Override
            public int compare(ListItem note1, ListItem note2) {
                if (!desc) {
                    return note1.getInterfaceTitle().toUpperCase().compareTo(note2.getInterfaceTitle().toUpperCase());
                } else {
                    return note2.getInterfaceTitle().toUpperCase().compareTo(note1.getInterfaceTitle().toUpperCase());
                }
            }

        });
        return myList;
    }

    public static ArrayList<EntryEmployee> sortByEmployeeName(ArrayList<EntryEmployee> myList, final boolean desc) {
        Collections.sort(myList, new Comparator<EntryEmployee>() {

            @Override
            public int compare(EntryEmployee note1, EntryEmployee note2) {
                if (!desc) {
                    return note1.getEmployeeName().toUpperCase().compareTo(note2.getEmployeeName().toUpperCase());
                } else {
                    return note2.getEmployeeName().toUpperCase().compareTo(note1.getEmployeeName().toUpperCase());
                }
            }

        });
        return myList;
    }


    public static ArrayList<ListItem> sortByCreateDate(ArrayList<ListItem> myList, final boolean desc) {
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

    public static ArrayList<ListItem> sortByModDate(ArrayList<ListItem> myList, final boolean desc) {
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
                    return Integer.compare(note1.getColor(), note2.getColor());
                } else {
                    return Integer.compare(note2.getColor(), note1.getColor());
                }
            }
        });
        return myList;
    }

    public static ArrayList<EntryEmployee> sortByTotalDebt(ArrayList<EntryEmployee> myList, final boolean desc) {
        Collections.sort(myList, new Comparator<EntryEmployee>() {

            @Override
            public int compare(EntryEmployee note1, EntryEmployee note2) {

                if (!desc) {
                    return Integer.compare((int) note1.getEmployeeSum(), (int) note2.getEmployeeSum());
                } else {
                    return Integer.compare((int) note2.getEmployeeSum(), (int) note1.getEmployeeSum());
                }
            }
        });
        return myList;
    }

    public static ArrayList<EntryWage> sortByPayment(ArrayList<EntryWage> myList, final boolean desc) {
        Collections.sort(myList, new Comparator<EntryWage>() {

            @Override
            public int compare(EntryWage note1, EntryWage note2) {

                if (!desc) {
                    return Integer.compare((int) note1.getWageWage(), (int) note2.getWageWage());
                } else {
                    return Integer.compare((int) note2.getWageWage(), (int) note1.getWageWage());
                }
            }
        });
        return myList;
    }




    public static boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }


    public static void toast(final Context context, final String text) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean isNumeric(final String str) {

        // null or empty
        if (str == null || str.length() == 0) {
            return false;
        }

            return str.chars().allMatch(Character::isDigit);

    }

    public static void tintMenuIcon(Context context, MenuItem item, int color) {
        Drawable normalDrawable = item.getIcon();
        Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
        DrawableCompat.setTint(wrapDrawable, color );


        item.setIcon(wrapDrawable);
    }

    public static void createNotificationChannel(Context ctx){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_ID,"Channel 1 ",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel1.setDescription("Pin to status bar channel.");

            NotificationManager manager = getSystemService(ctx,NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}





