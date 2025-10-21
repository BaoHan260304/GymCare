package base.api.model;


import base.api.model.user.UserModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity(name = "order_items")
public class OrderItemModel extends BaseModel  {
    private String productName;
    private double productPrice;
    private long productId;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private OrderModel orderModel;

}
