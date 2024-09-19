package com.bankingapp.dto;

import org.springframework.beans.BeanUtils;

import com.bankingapp.entity.User;

import lombok.Data;

@Data
public class RegisterUserRequestDto {

	private int id;

	private String name;

	private String email;

	private String password;

	private String roles;

	private String gender;

	private String contact;

	private String street;

	private String city;

	private String pincode;

	public static User toUserEntity(RegisterUserRequestDto registerUserRequestDto) {
		User user = new User();
		BeanUtils.copyProperties(registerUserRequestDto, user);
		return user;
	}

}
