package hu.spiralsoft.sims.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="products")
public class Product extends BaseEntity{
    private String name;
    private double quantity;
    private Date addedToInventory;
    private Date created;
    @OneToOne
    private User creator;
    @ManyToOne
    private Inventory inventory;
    @ElementCollection
    @CollectionTable(name = "products_properties",
            joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "property_name")
    @Column(name = "properties")
    private Map<String,String> dynProperties;

}
