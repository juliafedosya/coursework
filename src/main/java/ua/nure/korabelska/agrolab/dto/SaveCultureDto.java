package ua.nure.korabelska.agrolab.dto;

import lombok.Data;
import ua.nure.korabelska.agrolab.model.PlantDivision;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class SaveCultureDto {

    @NotNull
    @NotEmpty
    private String name;

    @Size(max = 240)
    private String description;

    @NotEmpty
    @NotNull
    private PlantDivision plantDivision;

    private Boolean visible;

    @NotNull
    private Set<@Min(1) Long> testAreasId;

}
