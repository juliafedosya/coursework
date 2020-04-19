package ua.nure.korabelska.agrolab.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AuthenticationRequestDto {
    @NotNull
    @NotEmpty
    private String username;
    @NotNull
    @NotEmpty
    private String password;
}
