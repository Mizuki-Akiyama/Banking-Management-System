package com.banking.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "Account")
@Data
public class Account {
    @Id
    @Column(name = "account_no")
    private long accountNo;
    
    @Column(name = "balance", precision = 15, scale = 2, columnDefinition = "DECIMAL(15,2) DEFAULT 0.00")
    private BigDecimal balance;
    
    @Column(name = "customer_id")
    private int customerId;
}