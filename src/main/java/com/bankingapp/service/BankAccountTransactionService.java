package com.bankingapp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bankingapp.dao.BankAccountDao;
import com.bankingapp.dao.BankAccountTransactionDao;
import com.bankingapp.dao.BeneficiaryDao;
import com.bankingapp.dao.UserDao;
import com.bankingapp.dto.BankTransactionRequestDto;
import com.bankingapp.dto.BankTransactionResponseDto;
import com.bankingapp.dto.BeneficiaryRequestDto;
import com.bankingapp.dto.CommonApiResponse;
import com.bankingapp.entity.BankAccount;
import com.bankingapp.entity.BankAccountTransaction;
import com.bankingapp.entity.Beneficiary;
import com.bankingapp.entity.User;
import com.bankingapp.utility.Constants.BankAccountStatus;
import com.bankingapp.utility.Constants.TransactionNarration;
import com.bankingapp.utility.Constants.TransactionType;
import com.bankingapp.utility.Constants.UserStatus;
import com.bankingapp.utility.Helper;

@Service
public class BankAccountTransactionService {

	private final Logger LOG = LoggerFactory.getLogger(BankAccountTransactionService.class);

	@Autowired
	private BankAccountTransactionDao bankAccountTransactionDao;

	@Autowired
	private BankAccountDao bankAccountDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private BeneficiaryDao beneficiaryDao;

	public ResponseEntity<CommonApiResponse> depositAmountTxn(BankTransactionRequestDto request) {

		LOG.info("Received request for deposit amount in customer account");

		CommonApiResponse response = new CommonApiResponse();

		if (request == null) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getAmount() == null || request.getSourceBankAccountId() == 0) {
			response.setResponseMessage("bad request, invalid or missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
			response.setResponseMessage("Failed to deposit amount, please select valid amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		BankAccount account = this.bankAccountDao.findById(request.getSourceBankAccountId()).orElse(null);

		if (account == null) {
			response.setResponseMessage("Bank Account found, enter correct account details!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (!account.getStatus().equals(BankAccountStatus.OPEN.value())) {
			response.setResponseMessage("Bank Account is Locked, Can't Deposit amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User user = account.getUser();

		if (!user.getStatus().equals(UserStatus.ACTIVE.value())) {
			response.setResponseMessage("User is not Active, Can't Deposit amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		account.setBalance(account.getBalance().add(request.getAmount()));
		BankAccount updateAccount = this.bankAccountDao.save(account);

		if (updateAccount == null) {
			response.setResponseMessage("Failed to deposit the amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		BankAccountTransaction transaction = new BankAccountTransaction();
		transaction.setType(TransactionType.DEPOSIT.value());
		transaction.setBankAccount(account);
		transaction.setAmount(request.getAmount());
		transaction.setNarration(TransactionNarration.BANK_DEPOSIT.value());
		transaction.setTransactionId(Helper.getAlphaNumericTransactionId());
		transaction.setTransactionTime(
				String.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
		transaction.setUser(user);

		BankAccountTransaction addedTxn = this.bankAccountTransactionDao.save(transaction);

		if (addedTxn == null) {
			response.setResponseMessage("Failed to save the Bank deposit transaction!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		else {
			response.setResponseMessage("Amount Deposited succesful!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
		}
	}

	public ResponseEntity<CommonApiResponse> withdrawAmountTxn(BankTransactionRequestDto request) {

		LOG.info("Received request for withdraw amount from customer account");

		CommonApiResponse response = new CommonApiResponse();

		if (request == null) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getAmount() == null || request.getSourceBankAccountId() == 0) {
			response.setResponseMessage("bad request, invalid or missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
			response.setResponseMessage("Failed to deposit amount, please select valid amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		BankAccount account = this.bankAccountDao.findById(request.getSourceBankAccountId()).orElse(null);

		if (account == null) {
			response.setResponseMessage("Bank Account found, enter correct account details!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (!account.getStatus().equals(BankAccountStatus.OPEN.value())) {
			response.setResponseMessage("Bank Account is Locked, Can't Withdraw amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (account.getBalance().compareTo(request.getAmount()) < 0) {
			response.setResponseMessage("Failed to withdraw amount, insufficient balance in customer account");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User user = account.getUser();

		if (!user.getStatus().equals(UserStatus.ACTIVE.value())) {
			response.setResponseMessage("User is not Active, Can't Withdraw amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		account.setBalance(account.getBalance().subtract(request.getAmount()));
		BankAccount updateAccount = this.bankAccountDao.save(account);

		if (updateAccount == null) {
			response.setResponseMessage("Failed to withdraw the amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		BankAccountTransaction transaction = new BankAccountTransaction();
		transaction.setType(TransactionType.WITHDRAW.value());
		transaction.setBankAccount(account);
		transaction.setAmount(request.getAmount());
		transaction.setNarration(TransactionNarration.BANK_WITHDRAW.value());
		transaction.setTransactionId(Helper.getAlphaNumericTransactionId());
		transaction.setTransactionTime(
				String.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
		transaction.setUser(user);

		BankAccountTransaction addedTxn = this.bankAccountTransactionDao.save(transaction);

		if (addedTxn == null) {
			response.setResponseMessage("Failed to save the Bank withdrawal transaction!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		else {
			response.setResponseMessage("Amount Withdraw succesful!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
		}
	}

	public ResponseEntity<CommonApiResponse> accountTransfer(BankTransactionRequestDto request) {

		LOG.info("Received request for customer account transfer");

		CommonApiResponse response = new CommonApiResponse();

		if (request == null) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getAmount() == null || request.getToBankAccount() == null || request.getToBankIfsc() == null) {
			response.setResponseMessage("bad request, invalid or missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
			response.setResponseMessage("Failed to deposit amount, please select valid amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		BankAccount senderAccount = this.bankAccountDao.findById(request.getSourceBankAccountId()).orElse(null);

		if (senderAccount == null) {
			response.setResponseMessage("Invalid Sender Bank Account!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (!senderAccount.getStatus().equals(BankAccountStatus.OPEN.value())) {
			response.setResponseMessage("Bank Account is Locked, Can't Transfer the Amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (senderAccount.getBalance().compareTo(request.getAmount()) < 0) {
			response.setResponseMessage("Insufficient Fund, Failed to transfer the amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		BankAccount reciepentAccount = this.bankAccountDao.findByNumberAndIfscCodeAndStatus(request.getToBankAccount(),
				request.getToBankIfsc(), BankAccountStatus.OPEN.value());

		if (reciepentAccount == null) {
			response.setResponseMessage("Receipent account not found, please enter the correct details and try again");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		senderAccount.setBalance(senderAccount.getBalance().subtract(request.getAmount()));
		BankAccount updateSenderAccount = this.bankAccountDao.save(senderAccount);

		if (updateSenderAccount == null) {
			response.setResponseMessage("Failed to transfer the amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		reciepentAccount.setBalance(reciepentAccount.getBalance().add(request.getAmount()));
		BankAccount updateReciepentAccount = this.bankAccountDao.save(reciepentAccount);

		if (updateReciepentAccount == null) {
			response.setResponseMessage("Failed to transfer the amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		BankAccountTransaction transaction = new BankAccountTransaction();
		transaction.setType(TransactionType.ACCOUNT_TRANSFER.value());
		transaction.setBankAccount(senderAccount);
		transaction.setDestinationBankAccount(reciepentAccount);
		transaction.setAmount(request.getAmount());
		transaction.setNarration(request.getAccountTransferPurpose());
		transaction.setTransactionId(Helper.getAlphaNumericTransactionId());
		transaction.setTransactionTime(
				String.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
		transaction.setUser(senderAccount.getUser());

		BankAccountTransaction addedTxn = this.bankAccountTransactionDao.save(transaction);

		if (addedTxn == null) {
			response.setResponseMessage("Failed to save the account transfer txn details in DB!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		else {
			response.setResponseMessage("Amount Transfer succesful!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
		}
	}

	public ResponseEntity<CommonApiResponse> customerBeneficiaryAdd(BeneficiaryRequestDto request) {

		LOG.info("Received request for customer account add");

		CommonApiResponse response = new CommonApiResponse();

		if (request == null || request.getUserId() == 0) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User user = this.userDao.findById(request.getUserId()).orElse(null);

		if (user == null) {
			response.setResponseMessage("user is not null!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Beneficiary beneficiary = new Beneficiary();
		beneficiary.setAccountNumber(request.getAccountNumber());
		beneficiary.setBankName(request.getBankName());
		beneficiary.setMaxTransferLimit(request.getMaxTransferLimit());
		beneficiary.setName(request.getName());
		beneficiary.setUser(user);

		Beneficiary addedBeneficiary = beneficiaryDao.save(beneficiary);

		if (addedBeneficiary == null) {
			response.setResponseMessage("Failed to add the Beneficiary!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		else {
			response.setResponseMessage("Beneficiary Added Successful!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
		}
	}

	public ResponseEntity<CommonApiResponse> customerBeneficiaryTransfer(BeneficiaryRequestDto request) {

		LOG.info("Received request for customer account transfer");

		CommonApiResponse response = new CommonApiResponse();

		if (request == null || request.getId() == 0) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Beneficiary beneficiary = this.beneficiaryDao.findById(request.getId()).orElse(null);

		if (beneficiary == null) {
			response.setResponseMessage("Beneficiary Account not found!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (new BigDecimal(beneficiary.getMaxTransferLimit()).compareTo(request.getAmount()) < 0) {
			response.setResponseMessage("You are exceeding the Max Limit for Selected Beneficiary!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User user = beneficiary.getUser();

		BankAccount bankAccount = this.bankAccountDao.findByUser_IdAndStatus(user.getId(),
				BankAccountStatus.OPEN.value());

		if (bankAccount.getBalance().compareTo(request.getAmount()) < 0) {
			response.setResponseMessage("Insufficient Balance in your Account!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		BankAccountTransaction transaction = new BankAccountTransaction();
		transaction.setType(TransactionType.BENEFICIARY_CREDIT.value());
		transaction.setBankAccount(bankAccount);
		// transaction.setDestinationBankAccount(reciepentAccount);
		transaction.setAmount(request.getAmount());
		transaction.setNarration(request.getNarration());
		transaction.setTransactionId(Helper.getAlphaNumericTransactionId());
		transaction.setTransactionTime(
				String.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
		transaction.setUser(user);
		transaction.setBeneficiary(beneficiary);

		bankAccount.setBalance(bankAccount.getBalance().subtract(request.getAmount()));

		bankAccountDao.save(bankAccount);

		BankAccountTransaction addedTxn = this.bankAccountTransactionDao.save(transaction);

		if (addedTxn == null) {
			response.setResponseMessage("Failed to save the account transfer txn details in DB!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		else {
			response.setResponseMessage("Amount credited to the Beneficiary!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
		}

	}

	public ResponseEntity<BankTransactionResponseDto> bankTransactionHistory(int userId) {

		LOG.info("Received request for fetching the user bank transaction history");

		BankTransactionResponseDto response = new BankTransactionResponseDto();

		if (userId == 0) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		User user = this.userDao.findById(userId).orElse(null);

		if (user == null) {
			response.setResponseMessage("user not found");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<BankAccountTransaction> bankAccountTransactions = new ArrayList<>();

		bankAccountTransactions = this.bankAccountTransactionDao.findByUser_idOrderByIdDesc(user.getId());

		if (bankAccountTransactions.isEmpty()) {
			response.setResponseMessage("No transaction found");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.OK);
		}

		response.setBankTransactions(bankAccountTransactions);
		response.setResponseMessage("Bank Transaction history fetched successfully");
		response.setSuccess(true);

		return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<BankTransactionResponseDto> allBankCustomerTransactions() {

		LOG.info("Received request for fetching all bank customer transaction");

		BankTransactionResponseDto response = new BankTransactionResponseDto();

		List<BankAccountTransaction> bankAccountTransactions = new ArrayList<>();

		bankAccountTransactions = this.bankAccountTransactionDao.findAll();

		if (bankAccountTransactions.isEmpty()) {
			response.setResponseMessage("No transaction found");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.OK);
		}

		response.setBankTransactions(bankAccountTransactions);
		response.setResponseMessage("Bank Transactions fetched successfully");
		response.setSuccess(true);

		return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.OK);

	}

}
