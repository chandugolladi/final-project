package com.bankingapp.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	private String email;

	@JsonIgnore
	private String password;

	private String roles;

	private String gender;

	private String contact;

	private String street;

	private String city;

	private String pincode;

	private String isAccountLinked; // Yes, No

	private String status; // active, deactivated

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Beneficiary> beneficiaries = new ArrayList<>();

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "account_id")
	private BankAccount account;

}
