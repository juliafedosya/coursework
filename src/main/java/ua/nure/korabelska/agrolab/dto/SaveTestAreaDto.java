package ua.nure.korabelska.agrolab.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class SaveTestAreaDto {

    @NotNull
    @NotEmpty
    String description;

}
