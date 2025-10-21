package base.api.dto.request;

import lombok.Data;

@Data
public class ProductDto {
    private long id;
    private String name;
    private String description;
    private double price;
    private double salePrice;
    private int quantity;
    private boolean isActive = true;
    private String image;
    private boolean allowSale = true;
    private Long categoryId;
}
