package base.api.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class TrainerCreationRequest {
    private String userName;
    private String email;
    private String mobile;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private String identityCard;
    private String licenceNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate licenceDate;
}