package ua.nure.korabelska.agrolab.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "cultures")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Culture extends BaseEntity {

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "plant_division")
    private PlantDivision plantDivision;

    @Column(name = "visible",columnDefinition = "boolean default false")
    private Boolean visible;

}
