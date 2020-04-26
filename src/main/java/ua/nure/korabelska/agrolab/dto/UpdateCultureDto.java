package ua.nure.korabelska.agrolab.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateCultureDto {

    @NotNull
    @NotEmpty
    private String name;

    @Size(max = 240)
    private String description;

}
