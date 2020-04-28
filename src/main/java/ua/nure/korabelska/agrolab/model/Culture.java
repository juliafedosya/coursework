package ua.nure.korabelska.agrolab.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cultures")
@Entity
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

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(mappedBy = "culture", cascade = CascadeType.REMOVE)
    private Set<TestArea>  testAreas;

    @EqualsAndHashCode.Exclude
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "project_id",referencedColumnName = "id")
    private Project project;


}
