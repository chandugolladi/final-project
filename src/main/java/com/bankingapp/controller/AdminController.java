package com.bankingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bankingapp.dto.AddBankAccountRequestDto;
import com.bankingapp.dto.BankTransactionRequestDto;
import com.bankingapp.dto.BankTransactionResponseDto;
import com.bankingapp.dto.CommonApiResponse;
import com.bankingapp.dto.UserListResponseDto;
import com.bankingapp.service.BankAccountService;
import com.bankingapp.service.BankAccountTransactionService;
import com.bankingapp.service.BeneficiaryService;
import com.bankingapp.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("api/bank/admin/")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

	@Autowired
	private BankAccountService bankAccountService;

	@Autowired
	private BankAccountTransactionService bankAccountTransactionService;

	@Autowired
	private UserService userService;

	@Autowired
	private BeneficiaryService beneficiaryService;

	@GetMapping("/fetch/role")
	@Operation(summary = "Api to get Users By Role")
	public ResponseEntity<UserListResponseDto> fetchAllBankUsers(@RequestParam("role") String role) {
		return userService.getUsersByRole(role);
	}

	@PostMapping("account/open")
	@Operation(summary = "Api to add customer bank account")
	public ResponseEntity<CommonApiResponse> addBankAccount(@RequestBody AddBankAccountRequestDto request) {
		return this.bankAccountService.addBankAccount(request);
	}

	@PostMapping("account/deposit")
	@Operation(summary = "Api for Bank transaction deposit")
	public ResponseEntity<CommonApiResponse> bankDepositTransaction(@RequestBody BankTransactionRequestDto request)
			throws Exception {
		return this.bankAccountTransactionService.depositAmountTxn(request);
	}

	@PostMapping("account/withdraw")
	@Operation(summary = "Api for Bank transaction withdraw")
	public ResponseEntity<CommonApiResponse> bankWithdrawTransaction(@RequestBody BankTransactionRequestDto request)
			throws Exception {
		return this.bankAccountTransactionService.withdrawAmountTxn(request);
	}

	@PostMapping("account/transfer")
	@Operation(summary = "Api for Bank Account transfer")
	public ResponseEntity<CommonApiResponse> accountTransferTransaction(@RequestBody BankTransactionRequestDto request)
			throws Exception {
		return this.bankAccountTransactionService.accountTransfer(request);
	}
	
	@GetMapping("transaction/all")
	@Operation(summary =  "Api for fetch bank transaction history")
	public ResponseEntity<BankTransactionResponseDto> getAllBankCustomerTransactions() {
		return this.bankAccountTransactionService.allBankCustomerTransactions();
	}

}
