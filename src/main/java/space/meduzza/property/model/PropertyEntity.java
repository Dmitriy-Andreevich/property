package space.meduzza.property.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "properties")
public class PropertyEntity extends BaseEntity {
    private String title;
    private String description;
    private String address;
    private Point coordinates;
    private int roomCount;
    private float square;
    private float cost;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private UserEntity creator;
    @NotNull
    @ToString.Exclude
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<MediaEntity> medias;
}
