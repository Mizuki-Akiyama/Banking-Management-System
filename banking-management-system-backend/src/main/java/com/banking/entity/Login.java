package com.banking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Login")
@Data
public class Login {
    @Id
    @Column(name = "login_id")
    private int loginId;
    
    @Column(name = "username", length = 50)
    private String username;
    
    @Column(name = "password", length = 50)
    private String password;
    
    @Column(name = "customer_id")
    private int customerId;
}