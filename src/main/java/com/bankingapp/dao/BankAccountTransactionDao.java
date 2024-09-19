package com.bankingapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankingapp.entity.BankAccountTransaction;

@Repository
public interface BankAccountTransactionDao extends JpaRepository<BankAccountTransaction, Integer> {

	List<BankAccountTransaction> findByBankAccount_id(int accountId);

	BankAccountTransaction findByTransactionId(String transactionId);

	List<BankAccountTransaction> findAllByOrderByIdDesc();

	List<BankAccountTransaction> findByUser_idOrderByIdDesc(int userId);

}
