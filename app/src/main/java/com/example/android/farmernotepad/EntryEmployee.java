package com.example.android.farmernotepad;

import java.util.ArrayList;

public class EntryEmployee {
    int employeeID;
    String employeeName;
    String employeePhone;
    double employeeSum;
    ArrayList<EntryWage> employeePaymentItems;

    public EntryEmployee(String employeeName, double employeeSum) {
        this.employeeName = employeeName;
        this.employeeSum = employeeSum;
    }

    public EntryEmployee() {
    }


    public void setEmployeeID(int ID) {
        this.employeeID = ID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeePhone(String phoneNumber) {
        this.employeePhone = phoneNumber;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeeSum(double sum) {
        this.employeeSum = sum;
    }

    public double getEmployeeSum() {
        return employeeSum;
    }

    public ArrayList<EntryWage> getEmployeePaymentItems() {
        return employeePaymentItems;
    }

    public void setEmployeePaymentItems(ArrayList<EntryWage> employeePaymentItems) {
        this.employeePaymentItems = employeePaymentItems;
    }


}
