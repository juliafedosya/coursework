package ua.nure.korabelska.agrolab.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@MappedSuperclass
@Data
@ToString(callSuper=true, includeFieldNames=true)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", columnDefinition = "timestamp default now()")
    private Date created;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
