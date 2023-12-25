package hu.spiralsoft.sims.security.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class addEmployeeRequest {
    private String email;

    private String placeOfBirth;
    private Date dateOfBirth;
    private String homeAddress;
    private String citizenship;
    private String identityCardNumber;
    private String socialSecurityNumber;
    private String phoneNumber;
}
