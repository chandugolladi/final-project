package com.bankingapp.dto;

import java.util.ArrayList;
import java.util.List;

import com.bankingapp.entity.User;

import lombok.Data;

@Data
public class UserListResponseDto extends CommonApiResponse {

	private List<User> users = new ArrayList<>();

}
