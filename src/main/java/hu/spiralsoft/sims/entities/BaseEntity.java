package hu.spiralsoft.sims.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.ReadOnlyProperty;


@MappedSuperclass
@Data
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="id")
    @ReadOnlyProperty
    protected Integer id;
}
