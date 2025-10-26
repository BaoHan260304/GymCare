package base.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
public class User extends BaseModel {

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(nullable = false)
    private String mobile;

    @Column(nullable = false)
    private java.time.LocalDate birthday;

    @Column(name = "identity_card", nullable = false)
    private String identityCard;

    @Column(name = "licence_number", nullable = false)
    private String licenceNumber;

    @Column(name = "licence_date", nullable = false)
    private java.time.LocalDate licenceDate;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "inactive", nullable = false, columnDefinition = "bit default 0")
    private boolean inactive = false;


    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
}
