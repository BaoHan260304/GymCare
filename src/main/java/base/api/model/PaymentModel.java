package base.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "payments")
@Data
public class PaymentModel extends BaseModel {
    private String orderCode;
    private Integer amount;
    private String description;
    private String checkoutUrl;
    private String status;
    private String qrCode;

    @OneToOne(mappedBy = "paymentModel",
            cascade = CascadeType.ALL, orphanRemoval = true
    )
    @JsonBackReference
    private OrderModel orderModel;

}
