package com.example.demo.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignUpDto {
    @NotBlank
    @Size(min = 3, max = 30)

    private String username;
    @NotBlank
    @Size(min = 6, max = 60)
    private String password;
}
