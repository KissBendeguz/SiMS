package hu.spiralsoft.sims.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;
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
    private String taxNumber;
    private String headQuarters;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date businessRegistrationDate;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date simsRegistrationDate;

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
