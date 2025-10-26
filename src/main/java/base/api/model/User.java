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

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
}
