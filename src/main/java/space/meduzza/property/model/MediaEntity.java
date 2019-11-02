package space.meduzza.property.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "medias")
public class MediaEntity extends BaseEntity {
    @NotNull
    @ManyToOne
    @JoinColumn
    private PropertyEntity property;

    @NotNull
    @Column(nullable = false)
    private String name;
}
