package base.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity(name ="cart_items")
@Data
public class CartItemModel extends BaseModel {
    private String productName;
    private double productPrice;
    private long productId;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonBackReference
    private CartModel cartModel;
}
