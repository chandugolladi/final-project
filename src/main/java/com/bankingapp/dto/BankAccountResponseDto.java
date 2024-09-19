package com.bankingapp.dto;

import java.util.List;

import com.bankingapp.entity.BankAccount;

import lombok.Data;

@Data
public class BankAccountResponseDto extends CommonApiResponse {

	private List<BankAccount> accounts;

}
