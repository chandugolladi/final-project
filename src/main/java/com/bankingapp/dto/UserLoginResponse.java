package com.bankingapp.dto;

import com.bankingapp.entity.User;

import lombok.Data;

@Data
public class UserLoginResponse extends CommonApiResponse {

	private User user;

	private String jwtToken;

}
