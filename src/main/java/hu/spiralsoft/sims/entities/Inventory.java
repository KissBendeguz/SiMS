package hu.spiralsoft.sims.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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
    @JoinColumn(name = "inventory_product_map")
    @JoinTable(
            name = "inventory_product_map",
            joinColumns = @JoinColumn(name = "inventory_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products;


}
