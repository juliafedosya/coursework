package ua.nure.korabelska.agrolab.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name = "projects")
@Data
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class Project extends BaseEntity {

    @OneToOne(mappedBy = "managerInProject")
    private User manager;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "participantInProject", cascade = CascadeType.ALL)
    private Set<User> members;

    @Column(name = "name")
    private String name;
//    @JsonIdentityInfo(
//            generator = ObjectIdGenerators.PropertyGenerator.class,
//            property = "name")
//    @JsonIdentityReference(alwaysAsId = true)

    //    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "manager_id",referencedColumnName = "id")
//    private User manager;


    //    @LazyCollection(LazyCollectionOption.FALSE)
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "user_projects",
//            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "land_plot_id", referencedColumnName = "id")}
//    )
}
