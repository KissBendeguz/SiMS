package hu.spiralsoft.sims.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String unit;
    private String itemNumber;
    private String category;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date addedToInventory;
    @ManyToOne
    @JoinColumn(name = "added_by_id")
    private User addedBy;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Inventory inventory;
    @ElementCollection
    @CollectionTable(name = "product_property_map",
            joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "property_key")
    @Column(name = "property")
    private Map<String,String> dynProperties;

}
