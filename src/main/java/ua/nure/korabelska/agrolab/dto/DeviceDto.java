package ua.nure.korabelska.agrolab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto {

    @Min(1) @Max(50)
    Integer temperature;

    @DecimalMin( value ="0.0")
    @DecimalMax(value = "1.0")
    Double humidity;

    @DecimalMin( value ="0.0")
    @DecimalMax(value = "1.0")
    Double acidity;

}
