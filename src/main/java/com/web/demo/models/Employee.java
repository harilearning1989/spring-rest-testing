package com.web.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "EMPLOYEE_DATA")
public class Employee {

    @Id
    @Column(name = "ID")
    private int id;
    @Column(name = "EMP_ID")
    private int empId;
    @Column(name = "EMP_NAME")
    private String empName;
    @Column(name = "FATHER_NAME")
    private String fatherName;
    @Column(name = "GENDER")
    private String gender;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "MANAGER_ID")
    private int manager_id;
    @Column(name = "SALARY")
    private int salary;
    @Column(name = "DESIGNATION")
    private String designation;

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getManager_id() {
        return manager_id;
    }

    public void setManager_id(int manager_id) {
        this.manager_id = manager_id;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", empId=" + empId +
                ", empName='" + empName + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", gender='" + gender + '\'' +
                ", category='" + category + '\'' +
                ", manager_id=" + manager_id +
                ", salary=" + salary +
                ", designation='" + designation + '\'' +
                '}';
    }
}
