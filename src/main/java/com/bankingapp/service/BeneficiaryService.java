package com.bankingapp.service;

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
import com.bankingapp.dto.BeneficiaryResponseDto;
import com.bankingapp.entity.Beneficiary;
import com.bankingapp.entity.User;

@Service
public class BeneficiaryService {

	private final Logger LOG = LoggerFactory.getLogger(BeneficiaryService.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private BeneficiaryDao beneficiaryDao;

	public ResponseEntity<BeneficiaryResponseDto> getCustomerBeneficiaries(int userId) {

		LOG.info("Received request for fetching bank by using User Id");

		BeneficiaryResponseDto response = new BeneficiaryResponseDto();

		List<Beneficiary> beneficiaries = new ArrayList<>();

		User user = this.userDao.findById(userId).orElse(null);

		if (user == null) {
			response.setResponseMessage("User not found!!!");
			response.setSuccess(true);
			return new ResponseEntity<BeneficiaryResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		beneficiaries = this.beneficiaryDao.findByUser(user);

		if (beneficiaries == null) {
			response.setResponseMessage("No Beneficiaries found for User");
			response.setSuccess(true);
			return new ResponseEntity<BeneficiaryResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		response.setBeneficiaries(beneficiaries);
		response.setResponseMessage("Beneficiaries Fetch Successfully!!!");
		response.setSuccess(true);

		return new ResponseEntity<BeneficiaryResponseDto>(response, HttpStatus.OK);

	}

}
