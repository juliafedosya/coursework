package ua.nure.korabelska.agrolab.model;

import lombok.*;
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

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "participantInProject", cascade = CascadeType.REMOVE)
    private Set<User> members;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private Set<Culture> cultures;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private Set<TestArea> testAreas;

    public void addMember(User user) {
        members.add(user);
    }
}
