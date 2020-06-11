package ua.nure.korabelska.agrolab.dto.update;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdateCultureDto {

    @NotNull
    @NotEmpty
    private String name;

    @Size(max = 240)
    private String description;

    private Boolean visible;

}
