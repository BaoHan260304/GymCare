package base.api.model;

import base.api.model.user.UserModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
public class CategoryModel extends BaseModel {
    private String name;
    private String description;

    private boolean isDeleted = false;
    // soft delete

    @OneToMany(mappedBy = "categoryModel",
            cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductModel> products = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name ="user_id")
    @JsonBackReference
    private UserModel userModel;

}
