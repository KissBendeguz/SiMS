package hu.spiralsoft.sims.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {
    private String firstname;
    private String lastname;
    private Gender gender;
    private String email;
    private String password;

    private String placeOfBirth;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date dateOfBirth;
    private String homeAddress;
    private String citizenship;
    private String identityCardNumber;
    private String socialSecurityNumber;
    private String phoneNumber;


    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "business_user_map")
    @JoinTable(
            name = "business_user_map",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "business_id"))
    private Set<Business> associatedBusinesses;


    public String getFullName(){
        return this.firstname+" "+this.lastname;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User otherUser = (User) obj;
        return id != null && id.equals(otherUser.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }



    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }


    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }


    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}