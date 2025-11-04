package com.banking.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "Loan")
@Data
public class Loan {
    @Id
    @Column(name = "loan_id")
    private int loanId;
    
    @Column(name = "loan_type", nullable = false, length = 50)
    private String loanType;
    
    @Column(name = "loan_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal loanAmount;
    
    @Column(name = "status", length = 10, columnDefinition = "ENUM('Pending','Approved','Rejected') DEFAULT 'Pending'")
    private String status;
    
    @Column(name = "customer_id")
    private int customerId;
}