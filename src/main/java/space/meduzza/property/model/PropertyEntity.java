package space.meduzza.property.model;

import lombok.*;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    @ToString.Exclude
    @OneToMany(mappedBy = "property")
    private List<MediaEntity> medias;
}
