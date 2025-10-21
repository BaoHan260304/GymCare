package base.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "carts")
public class CartModel extends BaseModel {
    private double totalPrice;
    private long userId;
    private long paymentId;

    @OneToMany(mappedBy = "cartModel",
            cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CartItemModel> cartItems = new ArrayList<>();
}
