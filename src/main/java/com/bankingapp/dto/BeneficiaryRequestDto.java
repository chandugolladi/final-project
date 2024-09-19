package com.bankingapp.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BeneficiaryRequestDto {

	private int id;

	private BigDecimal amount;

	private String narration;

	private String name;

	private String bankName;

	private String accountNumber;

	private String maxTransferLimit;

	private int userId;

}
