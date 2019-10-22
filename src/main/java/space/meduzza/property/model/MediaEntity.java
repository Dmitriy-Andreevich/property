package space.meduzza.property.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "medias")
public class MediaEntity extends BaseEntity {
    @ManyToOne()
    @JoinColumn
    private PropertyEntity property;
    @Column(nullable = false)
    private String name;
}
