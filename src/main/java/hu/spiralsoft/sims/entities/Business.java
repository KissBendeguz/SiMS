package hu.spiralsoft.sims.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;
@Getter
@Setter
@Accessors
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="businesses")
public class Business extends BaseEntity{
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User owner;


    @ManyToMany(mappedBy = "associatedBusinesses" ,fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<User> associates;

}
