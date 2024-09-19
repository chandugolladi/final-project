package com.bankingapp.dto;

import java.util.ArrayList;
import java.util.List;

import com.bankingapp.entity.Beneficiary;

import lombok.Data;

@Data
public class BeneficiaryResponseDto extends CommonApiResponse {

	private List<Beneficiary> beneficiaries = new ArrayList();

}
