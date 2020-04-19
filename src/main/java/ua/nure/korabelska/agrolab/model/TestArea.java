package ua.nure.korabelska.agrolab.model;

import javax.persistence.*;


@Entity
@Table(name = "test_areas")
public class TestArea extends BaseEntity {

    @Column(name = "description")
    String description;

    @ManyToOne
    @JoinColumn(name = "culture_id", referencedColumnName = "id")
    Culture currentCulture;

    @Column(name = "observation_data")
    String observationData;

}
