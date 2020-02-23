package com.example.android.farmernotepad;

public class WageEntry {

    int wageID;
    int employee_rel_ID;
    double wageWage;
    String wageDesc;
    double wageHours;
    String wageWorkDate;
    String wageCreateDate;
    int wageType;

    public WageEntry() {
    }

    public WageEntry(double wageWage, String wageDesc, double wageHours, String wageWorkDate) {
        this.wageWage = wageWage;
        this.wageDesc = wageDesc;
        this.wageHours = wageHours;
        this.wageWorkDate = wageWorkDate;
    }

    public int getWageID() {
        return wageID;
    }

    public int getEmployee_rel_ID() {
        return employee_rel_ID;
    }

    public void setEmployee_rel_ID(int employee_rel_ID) {
        this.employee_rel_ID = employee_rel_ID;
    }

    public double getWageWage() {
        return wageWage;
    }

    public void setWageWage(double wageWage) {
        this.wageWage = wageWage;
    }

    public String getWageDesc() {
        return wageDesc;
    }

    public void setWageDesc(String wageDesc) {
        this.wageDesc = wageDesc;
    }

    public double getWageHours() {
        return wageHours;
    }

    public void setWageHours(double wageHours) {
        this.wageHours = wageHours;
    }

    public String getWageWorkDate() {
        return wageWorkDate;
    }

    public void setWageWorkDate(String wageWorkDate) {
        this.wageWorkDate = wageWorkDate;
    }

    public String getWageCreateDate() {
        return wageCreateDate;
    }

    public void setWageCreateDate(String wageCreateDate) {
        this.wageCreateDate = wageCreateDate;
    }

    public int getWageType() {
        return wageType;
    }

    public void setWageType(int wageType) {
        this.wageType = wageType;
    }

    public void setWageID(int wageID) {
        this.wageID = wageID;
    }
}
