package com.example.android.farmernotepad;

public class Employee {
    int employeeID;
    String employeeName;
    int employeePhone;
    double employeeSum;

    public Employee(String employeeName, double employeeSum) {
        this.employeeName = employeeName;
        this.employeeSum = employeeSum;
    }

    public Employee(){};

    public void setEmployeeID(int ID){
        this.employeeID = ID;
    }

    public int getEmployeeID(){
        return employeeID;
    }

    public void setEmployeeName(String employeeName){
        this.employeeName = employeeName;
    }

    public String getEmployeeName(){
        return employeeName;
    }

    public void setEmployeePhone(int phoneNumber){
        this.employeePhone = phoneNumber;
    }

    public int getEmployeePhone(){
        return employeePhone;
    }

    public void setEmployeeSum(double sum){
        this.employeeSum = sum;
    }

    public double getEmployeeSum(){
        return employeeSum;
    }
}
