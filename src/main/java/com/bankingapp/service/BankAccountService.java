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
import com.bankingapp.dao.BeneficiaryDao;
import com.bankingapp.dao.UserDao;
import com.bankingapp.dto.AddBankAccountRequestDto;
import com.bankingapp.dto.BankAccountResponseDto;
import com.bankingapp.dto.BeneficiaryResponseDto;
import com.bankingapp.dto.CommonApiResponse;
import com.bankingapp.entity.BankAccount;
import com.bankingapp.entity.Beneficiary;
import com.bankingapp.entity.User;
import com.bankingapp.utility.Constants.BankAccountStatus;
import com.bankingapp.utility.Constants.IsAccountLinked;

@Service
public class BankAccountService {

	private final Logger LOG = LoggerFactory.getLogger(BankAccountService.class);

	@Autowired
	private BankAccountDao bankAccountDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private BeneficiaryDao beneficiaryDao;

	public ResponseEntity<CommonApiResponse> addBankAccount(AddBankAccountRequestDto request) {

		LOG.info("Received request for add bank account");

		CommonApiResponse response = new CommonApiResponse();

		if (request == null) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getUserId() == 0) {
			response.setResponseMessage("bad request, user id is null");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		BankAccount account = AddBankAccountRequestDto.toBankAccountEntity(request);

		User user = this.userDao.findById(request.getUserId()).orElse(null);

		if (user == null) {
			response.setResponseMessage("Customer not found!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		account.setUser(user);

		account.setStatus(BankAccountStatus.OPEN.value());
		account.setCreationDate(
				String.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
		account.setBalance(BigDecimal.ZERO);

		BankAccount addedBankAccount = this.bankAccountDao.save(account);

		if (addedBankAccount != null) {

			user.setIsAccountLinked(IsAccountLinked.YES.value());
			user.setAccount(addedBankAccount);
			this.userDao.save(user);

			response.setResponseMessage("Bank Account Created Successfully!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
		} else {
			response.setResponseMessage("Failed to add the bank account");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

	}

	public ResponseEntity<BankAccountResponseDto> getCustomerAccountId(int userId) {

		LOG.info("Received request for fetching bank by using User Id");

		BankAccountResponseDto response = new BankAccountResponseDto();

		List<BankAccount> accounts = new ArrayList<>();

		if (userId == 0) {
			response.setResponseMessage("bad request, user id is missing");
			response.setSuccess(true);

			return new ResponseEntity<BankAccountResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		BankAccount account = this.bankAccountDao.findByUser_Id(userId);

		if (account == null) {
			response.setResponseMessage("No Bank Account found for User");
			response.setSuccess(true);
			return new ResponseEntity<BankAccountResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		accounts.add(account);

		response.setAccounts(accounts);
		response.setResponseMessage("Bank Accounts Fetch Successfully!!!");
		response.setSuccess(true);

		return new ResponseEntity<BankAccountResponseDto>(response, HttpStatus.OK);

	}


}
