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

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;


    @ManyToMany(mappedBy = "associatedBusinesses" ,fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<User> associates;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "invitations",
            joinColumns = @JoinColumn(name = "business_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> invitedUsers;

}
