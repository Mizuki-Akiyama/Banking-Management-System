package com.banking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Branch")
@Data
public class Branch {
    @Id
    @Column(name = "branch_id")
    private int branchId;
    
    @Column(name = "branch_name", nullable = false, length = 100)
    private String branchName;
    
    @Column(name = "branch_address", nullable = false, length = 250)
    private String branchAddress;
}