package base.api.model;

import base.api.model.user.UserModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class OrderModel  extends BaseModel {
    private double totalPrice;
    private String title;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private UserModel userModel;

    @OneToOne()
    @JoinColumn(name = "payment_id", unique = true)
    private PaymentModel paymentModel;

    @OneToMany(mappedBy = "orderModel",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemModel> orderItems = new ArrayList<>();

}
