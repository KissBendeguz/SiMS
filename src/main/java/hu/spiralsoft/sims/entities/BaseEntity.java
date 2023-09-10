package hu.spiralsoft.sims.entities;

import jakarta.persistence.*;
import lombok.Data;


@MappedSuperclass
@Data
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="id")
    protected Integer id;
}
