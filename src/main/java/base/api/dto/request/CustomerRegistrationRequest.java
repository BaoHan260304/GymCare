package base.api.dto.request;

import lombok.Data;

@Data
public class CustomerRegistrationRequest {
    private String userName;
    private String email;
    private String password;
    private String mobile;
    private String birthday; // Keep as String to match original service
    private String identityCard;
}