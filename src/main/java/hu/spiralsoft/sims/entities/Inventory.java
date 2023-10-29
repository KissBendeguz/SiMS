package hu.spiralsoft.sims.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "inventories")
public class Inventory extends BaseEntity{
    private String name;
    @ManyToOne
    private Business business;
    @OneToMany
    private HashSet<Product> products;


}
