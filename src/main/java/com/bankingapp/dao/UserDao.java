package com.bankingapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankingapp.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

	User findByEmailAndPassword(String emailId, String password);

	User findByEmailAndPasswordAndRoles(String emailId, String password, String role);

	User findByEmailAndRoles(String emailId, String role);

	User findByEmail(String emailId);

	List<User> findByRolesAndStatus(String role, String status);

	List<User> findByRoles(String role);

	List<User> findByNameContainingIgnoreCaseAndRoles(String customerName, String role);

}
