package com.bankingapp.dto;

import java.util.ArrayList;
import java.util.List;

import com.bankingapp.entity.BankAccountTransaction;

import lombok.Data;

@Data
public class BankTransactionResponseDto extends CommonApiResponse {

	private List<BankAccountTransaction> bankTransactions = new ArrayList<>();

}
