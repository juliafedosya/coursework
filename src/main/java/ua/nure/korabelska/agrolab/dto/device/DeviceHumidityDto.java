package ua.nure.korabelska.agrolab.dto.device;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeviceHumidityDto {

  @Min(0)
  @Max(99)
  @NotNull
  private Integer humidity;

}
