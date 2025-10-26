package base.api.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String mobile;
    private LocalDate birthday;
    private String identityCard;
    private String licenceNumber;
    private LocalDate licenceDate;
}