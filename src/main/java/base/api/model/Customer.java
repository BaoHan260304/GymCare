package base.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "Customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CustomerID")
    private Integer customerId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "UserID", nullable = false, unique = true)
    private User user;

    // Tạm thời để null, sẽ cập nhật khi có module Membership
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MembershipID")
    private Membership membership;

    @Column(name = "JoinDate", nullable = false)
    private LocalDate joinDate;
}