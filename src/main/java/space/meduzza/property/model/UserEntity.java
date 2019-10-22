package space.meduzza.property.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Email;
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
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String authorities;
    @ToString.Exclude
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<PropertyEntity> properties;
}
