package base.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "staff")
@AttributeOverride(name = "id", column = @Column(name = "staff_id"))
public class Staff extends BaseModel {

    @Column(name = "licence_number")
    private String licenceNumber;

    @Column(name = "licence_date")
    private java.time.LocalDate licenceDate;

    // You can add more staff-specific fields here later, like hire_date, salary, etc.

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}