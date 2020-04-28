package ua.nure.korabelska.agrolab.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateTestAreaDto {

    @NotNull
    @NotEmpty
    String observationData;

    @NotNull
    Long cultureId;
}
