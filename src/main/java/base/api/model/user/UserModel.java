package base.api.model.user;


import base.api.enums.UserGender;
import base.api.enums.UserRole;
import base.api.model.BaseModel;
import base.api.model.CategoryModel;
import base.api.model.OrderModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 Các @Entity là JPA annotation được sử dụng để đánh dấu một lớp Java như một thực thể (entity) trong cơ sở dữ liệu.
 */

@Data
@Entity
@Table(name = "user")
public class UserModel extends BaseModel {

    public String userName;

    public String phone;

    public String firstName;

    public UserGender gender;

    public String lastName;

    public LocalDateTime birthDate;

    public String avatar;

    public boolean isActive = true;

    public String email;

    public String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private UserRole role;

    @OneToMany(
            mappedBy = "userModel",
            cascade = CascadeType.ALL, orphanRemoval = true
    )
    @JsonManagedReference
    private List<CategoryModel> categories = new ArrayList<>();

    @OneToMany(
            mappedBy = "userModel",
            cascade = CascadeType.ALL, orphanRemoval = true
    )
    private List<OrderModel> orders = new ArrayList<>();

}
