package base.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "Memberships")
@Getter
@Setter
@NoArgsConstructor
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MembershipID")
    private Integer id;

    @Column(name = "MembershipName", nullable = false, length = 100)
    private String membershipName;

    @Column(name = "Description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "Price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "DurationInDays", nullable = false)
    private Integer durationInDays;

    @Column(name = "IsActive", nullable = false)
    private boolean isActive = true;
}