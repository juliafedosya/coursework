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
public class DeviceAcidityDto {

  @Min(1)
  @Max(14)
  @NotNull
  private Integer acidity;

}
