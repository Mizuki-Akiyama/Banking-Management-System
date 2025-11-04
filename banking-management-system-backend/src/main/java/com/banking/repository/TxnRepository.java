package com.banking.repository;

import com.banking.entity.Txn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TxnRepository extends JpaRepository<Txn, Integer> {
}