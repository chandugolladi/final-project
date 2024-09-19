package com.bankingapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankingapp.entity.BankAccount;

@Repository
public interface BankAccountDao extends JpaRepository<BankAccount, Integer> {

	BankAccount findByUser_IdAndStatus(int userId, String status);

	List<BankAccount> findByStatus(String status);

	BankAccount findByNumberAndIfscCodeAndStatus(String accNumber, String ifscCode, String Status);

	BankAccount findByUser_Id(int userId);

	List<BankAccount> findByNumberContainingIgnoreCase(String accountNumber);

}
