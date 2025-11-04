package com.banking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Employee")
@Data
public class Employee {
    @Id
    @Column(name = "emp_id")
    private int empId;
    
    @Column(name = "emp_name", nullable = false, length = 100)
    private String empName;
    
    @Column(name = "emp_position", nullable = false, length = 50)
    private String empPosition;
    
    @Column(name = "branch_id")
    private int branchId;
}