package ua.nure.korabelska.agrolab.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "cultures")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Culture extends BaseEntity {

}
