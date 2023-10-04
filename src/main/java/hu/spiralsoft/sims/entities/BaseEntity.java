package hu.spiralsoft.sims.entities;

import jakarta.persistence.*;
import org.springframework.data.annotation.ReadOnlyProperty;


@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="id")
    @ReadOnlyProperty
    protected Integer id;
}
