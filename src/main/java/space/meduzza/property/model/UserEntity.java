package space.meduzza.property.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity{
    @Email
    private String email;
    private String password;
    private String authorities;
    @ToString.Exclude
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<PropertyEntity> properties;
}
