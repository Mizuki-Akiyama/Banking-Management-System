package com.banking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Customer")
@Data
public class Customer {
    @Id
    @Column(name = "customer_id")
    private int customerId;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    
    @Column(name = "phone", nullable = false, length = 30)
    private String phone;
    
    @Column(name = "address", nullable = false, length = 250)
    private String address;
    
    @Column(name = "branch_id")
    private int branchId;
}