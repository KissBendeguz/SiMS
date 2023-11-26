package hu.spiralsoft.sims.security.http;

import hu.spiralsoft.sims.entities.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Gender gender;

    @Override
    public String toString(){
        return String.format("{\"email\":\"%s\",\"password\":\"%s\"}",this.email,this.password);
    }
}
