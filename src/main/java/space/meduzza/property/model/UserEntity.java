package space.meduzza.property.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {
    private String fname;
    private String lname;
    private String email;
    private String password;
    private String authorities;
    @ToString.Exclude
    @OneToMany(mappedBy = "creator")
    private List<PropertyEntity> properties;
}
