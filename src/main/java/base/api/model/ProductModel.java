package base.api.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class ProductModel extends BaseModel {
    private String name;
    private String description;
    private double price;
    private double salePrice;
    private double quantity;
    private boolean isActive = true;
    private String image;
    private boolean allowSale = true;

    @ManyToOne()
    @JoinColumn(name ="category_id")
    @JsonBackReference
    private CategoryModel categoryModel;
}
