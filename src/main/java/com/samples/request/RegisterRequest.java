package com.samples.request;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class RegisterRequest {
    
    @NotBlank
    @Length(min=1, max=255)
    private String firstName;
    
    @NotBlank
    @Length(min=1, max=255)
    private String lastName;
    
    @NotBlank
    @Digits(integer = 9, fraction = 0)
    private String telephone;
    
    @NotBlank
    @Length(min=1, max=255)
    private String username;
    
    @NotBlank
    @Length(min=1, max=255)
    private String password;
    
    @NotBlank
    @Length(min=1, max=255)
    private String plan;
}
