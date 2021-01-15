package com.softserve.teachua.dto.service;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserLogin {

	@NotEmpty
	private String email;

	@NotEmpty
	private String password;

}
