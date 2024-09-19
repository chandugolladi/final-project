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

import com.bankingapp.dto.BankAccountResponseDto;
import com.bankingapp.dto.BankTransactionResponseDto;
import com.bankingapp.dto.BeneficiaryRequestDto;
import com.bankingapp.dto.BeneficiaryResponseDto;
import com.bankingapp.dto.CommonApiResponse;
import com.bankingapp.dto.RegisterUserRequestDto;
import com.bankingapp.dto.UserListResponseDto;
import com.bankingapp.dto.UserLoginRequest;
import com.bankingapp.dto.UserLoginResponse;
import com.bankingapp.service.BankAccountService;
import com.bankingapp.service.BankAccountTransactionService;
import com.bankingapp.service.BeneficiaryService;
import com.bankingapp.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("api/bank/customer")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {

	@Autowired
	private BankAccountTransactionService bankAccountTransactionService;

	@Autowired
	private UserService userService;

	@Autowired
	private BeneficiaryService beneficiaryService;

	@Autowired
	private BankAccountService bankAccountService;

	@PostMapping("register")
	@Operation(summary = "Api to register customer and admin")
	public ResponseEntity<CommonApiResponse> registerUser(@RequestBody RegisterUserRequestDto request) {
		return this.userService.registerUser(request);
	}

	@PostMapping("login")
	@Operation(summary = "Api to login any User")
	public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
		return userService.login(userLoginRequest);
	}

//	@PostMapping("profile/update")
//	@Operation(summary = "Api to update the user profile")
//	public ResponseEntity<CommonApiResponse> updateProfile(@RequestBody RegisterUserRequestDto request) {
//		return this.userService.updateProfile(request);
//	}

	@PostMapping("beneficiary/add")
	@Operation(summary = "Api for transfer money to the beneficiary")
	public ResponseEntity<CommonApiResponse> customerBeneficiaryAdd(@RequestBody BeneficiaryRequestDto request)
			throws Exception {
		return this.bankAccountTransactionService.customerBeneficiaryAdd(request);
	}

	@GetMapping("beneficiary/fetch")
	@Operation(summary = "Api for fetch customer beneficiaries")
	public ResponseEntity<BeneficiaryResponseDto> getCustomerBeneficiaries(@RequestParam("userId") int userId) {
		return this.beneficiaryService.getCustomerBeneficiaries(userId);
	}

	@PostMapping("beneficiary/transfer")
	@Operation(summary = "Api for transfer money to the beneficiary")
	public ResponseEntity<CommonApiResponse> customerBeneficiaryTransfer(@RequestBody BeneficiaryRequestDto request)
			throws Exception {
		return this.bankAccountTransactionService.customerBeneficiaryTransfer(request);
	}

	@GetMapping("transaction/history")
	@Operation(summary = "Api for fetch bank transaction history")
	public ResponseEntity<BankTransactionResponseDto> getUserBankTransactionHistory(
			@RequestParam("userId") int userId) {
		return this.bankAccountTransactionService.bankTransactionHistory(userId);
	}

	@GetMapping("account/fetch")
	@Operation(summary = "Api for fetch bank transaction history")
	public ResponseEntity<BankAccountResponseDto> getCustomerAccountId(@RequestParam("userId") int userId) {
		return this.bankAccountService.getCustomerAccountId(userId);
	}
	
	@GetMapping("/fetch")
	@Operation(summary = "Api for fetch customer by Id")
	public ResponseEntity<UserListResponseDto> getUserDetail(@RequestParam("userId") int userId) {
		return this.userService.getCustomerBeneficiaries(userId);
	}
	
	@PostMapping("/profile/update")
	@Operation(summary = "Api to udpate the profile")
	public ResponseEntity<CommonApiResponse> profileUpdate(@RequestBody RegisterUserRequestDto request) {
		return this.userService.profileUpdate(request);
	}

}
