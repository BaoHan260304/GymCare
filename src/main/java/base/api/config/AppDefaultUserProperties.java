package base.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "app.default-users")
@Data
public class AppDefaultUserProperties {

    private Map<String, DefaultUser> Map;

    @Data
    public static class DefaultUser {
        private String name;
        private String email;
        private String password;
        private String mobile;
        private String birthday;
        private String identitycard;
        private String licencenumber;
        private String licencedate;
    }
}
