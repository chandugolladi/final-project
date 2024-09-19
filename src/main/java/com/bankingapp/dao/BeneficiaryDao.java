package com.bankingapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankingapp.entity.Beneficiary;
import com.bankingapp.entity.User;

@Repository
public interface BeneficiaryDao extends JpaRepository<Beneficiary, Integer> {
	
	List<Beneficiary> findByUser(User user);

}
