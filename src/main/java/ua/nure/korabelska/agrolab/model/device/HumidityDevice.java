package ua.nure.korabelska.agrolab.model.device;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import ua.nure.korabelska.agrolab.model.TestArea;

@Entity
@Table(name = "humidity_device")
@Data
public class HumidityDevice {

  @Id
  @Column(name = "id")
  private Long id;

  private Boolean active;

  @Column(name = "humidity")
  private Integer humidity;

  @JsonIgnore
  @OneToOne
  @MapsId
  private TestArea testArea;

}
