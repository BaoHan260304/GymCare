package base.api.dto.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String userName;
    private String email;
}