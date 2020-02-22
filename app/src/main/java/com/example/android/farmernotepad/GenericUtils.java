package com.example.android.farmernotepad;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
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

    public static ArrayList<Employee> sortByEmployeeName(ArrayList<Employee> myList, final boolean desc) {
        Collections.sort(myList, new Comparator<Employee>() {

            @Override
            public int compare(Employee note1, Employee note2) {
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

    public static ArrayList<Employee> sortByTotalDebt(ArrayList<Employee> myList, final boolean desc) {
        Collections.sort(myList, new Comparator<Employee>() {

            @Override
            public int compare(Employee note1, Employee note2) {

                if (!desc) {
                    return Integer.compare((int) note1.getEmployeeSum(), (int) note2.getEmployeeSum());
                } else {
                    return Integer.compare((int) note2.getEmployeeSum(), (int) note1.getEmployeeSum());
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
}





