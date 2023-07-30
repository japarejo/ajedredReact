package com.samples.request;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
	
	@NotBlank
	@Length(min=1, max=255)
	private String username;
	
	@NotBlank
	@Length(min=1, max=255)
	private String password;

}
