package com.banking.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Txn")
@Data
public class Txn {
    @Id
    @Column(name = "txn_no")
    private int txnNo;
    
    @Column(name = "txn_type", nullable = false, length = 10)
    private String txnType;
    
    @Column(name = "tnx_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime tnxDate;
    
    @Column(name = "amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;
    
    @Column(name = "account_no")
    private long accountNo;
}