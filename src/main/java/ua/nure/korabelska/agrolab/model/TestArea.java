package ua.nure.korabelska.agrolab.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;


@Data
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "test_areas")
public class TestArea extends BaseEntity {

    @Column(name = "description")
    String description;

    @ManyToOne
    @JoinColumn(name = "culture_id", referencedColumnName = "id")
    Culture culture;

    @EqualsAndHashCode.Exclude
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    Project project;

    @Column(name = "observation_data")
    String observationData;

}
