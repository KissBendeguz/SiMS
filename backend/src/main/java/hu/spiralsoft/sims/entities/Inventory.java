package hu.spiralsoft.sims.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String address;
    private String managerName;
    private String managerPhone;
    private String managerEmail;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Business business;
    @OneToMany
    @JoinColumn(name = "inventory_product_map")
    @JoinTable(
            name = "inventory_product_map",
            joinColumns = @JoinColumn(name = "inventory_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products;


}
