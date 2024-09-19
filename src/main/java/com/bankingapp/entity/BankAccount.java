package com.bankingapp.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class BankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String number;

	private String ifscCode;

	private String type; // saving or current

	private BigDecimal balance;

	private String creationDate;

	private String status;

	// One-to-One mapping with User
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user; // 1 user can 1 bank account

}
