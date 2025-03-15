package com.web.demo.utils;

public class EmployeeUtils {
    public static String getInitials(String empName, String fatherName) {
        return empName + fatherName;
    }

    public static String sendEmail(){
        return "Success";
    }

    public static String getEmployeeLevel(int experience) {
        return experience >= 5 ? "Senior" : "Junior";
    }

    public static double calculateTax(double salary) {
        return salary * 0.20;
    }

}
