package hu.spiralsoft.sims.security.http;

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
    /*private String firstname;
    @Nullable
    private String middlename;
    private String lastname;*/
    private String email;
    private String password;

    @Override
    public String toString(){
        return String.format("{\"email\":\"%s\",\"password\":\"%s\"}",this.email,this.password);
    }
}
