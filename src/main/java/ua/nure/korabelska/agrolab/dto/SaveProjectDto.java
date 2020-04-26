package ua.nure.korabelska.agrolab.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveProjectDto {

    @NotEmpty
    @NotNull
    private String name;

    @NotNull
    private Set<@Min(1) Long> membersId;

//    @NotNull
//    @Min(1)
//    private Long managerId;

}
