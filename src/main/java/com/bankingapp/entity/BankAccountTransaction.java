package com.bankingapp.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class BankAccountTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	// Many-to-One mapping with BankAccount
	@ManyToOne
	@JoinColumn(name = "bank_account_id")
	private BankAccount bankAccount;

	private String type; // withdraw, deposit, account transfer - Admin
							// beneficiaries credit - Customer

	private String transactionId; // unique transaction id

	private BigDecimal amount;

	// Many-to-One mapping with BankAccount (Destination Account for Account
	// Transfer)
	@ManyToOne
	@JoinColumn(name = "destination_bank_account_id")
	private BankAccount destinationBankAccount; // account transfer by admin then only present
	
	@ManyToOne
	@JoinColumn(name = "beneficiary_id")
	private Beneficiary beneficiary;

	private String transactionTime;

	private String narration;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

}
