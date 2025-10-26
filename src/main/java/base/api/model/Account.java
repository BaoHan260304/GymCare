package base.api.model;

import base.api.model.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")

@AttributeOverride(name = "id", column = @Column(name = "account_id"))
public class Account extends BaseModel {

    @Column(name = "account_name", nullable = false, unique = true)
    private String accountName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // Ví dụ: "Admin", "Customer"

    @Column(name = "is_active", nullable = false, columnDefinition = "bit default 1")
    private boolean isActive = true;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private User user;
}