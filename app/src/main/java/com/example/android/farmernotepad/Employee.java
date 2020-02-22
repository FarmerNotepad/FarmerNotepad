package com.example.android.farmernotepad;

import java.util.ArrayList;

public class Employee {
    int employeeID;
    String employeeName;
    double employeePhone;
    double employeeSum;
    ArrayList<WageEntry> employeePaymentItems;

    public Employee(String employeeName, double employeeSum) {
        this.employeeName = employeeName;
        this.employeeSum = employeeSum;
    }

    public Employee() {
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

    public void setEmployeePhone(double phoneNumber) {
        this.employeePhone = phoneNumber;
    }

    public double getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeeSum(double sum) {
        this.employeeSum = sum;
    }

    public double getEmployeeSum() {
        return employeeSum;
    }

    public ArrayList<WageEntry> getEmployeePaymentItems() {
        return employeePaymentItems;
    }

    public void setEmployeePaymentItems(ArrayList<WageEntry> employeePaymentItems) {
        this.employeePaymentItems = employeePaymentItems;
    }
}
