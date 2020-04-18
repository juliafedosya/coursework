package ua.nure.korabelska.agrolab.model;

import javax.persistence.*;


@Entity
@Table(name = "test_areas")
public class TestArea extends BaseEntity {

    @Column
    String description;

    @ManyToOne
    @JoinColumn(name = "culture_id", referencedColumnName = "id")
    Culture currentCulture;



}
