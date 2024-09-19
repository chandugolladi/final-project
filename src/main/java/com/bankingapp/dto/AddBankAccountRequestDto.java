package com.bankingapp.dto;

import org.springframework.beans.BeanUtils;

import com.bankingapp.entity.BankAccount;

public class AddBankAccountRequestDto {

	private String number;

	private String ifscCode;

	private String type; // saving or current

	private int userId; // bank customer id

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public static BankAccount toBankAccountEntity(AddBankAccountRequestDto addBankAccountRequestDto) {
		BankAccount bankAccount = new BankAccount();
		BeanUtils.copyProperties(addBankAccountRequestDto, bankAccount,  "userId");
		return bankAccount;
	}

}
