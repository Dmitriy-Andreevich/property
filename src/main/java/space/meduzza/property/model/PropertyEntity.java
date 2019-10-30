package space.meduzza.property.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "properties")
public class PropertyEntity extends BaseEntity  {
    @Size(min = 3, max = 100)
    @NotBlank
    @Column(nullable = false)
    private String title;
    @Size(max = 10000)
    @NotNull
    @Column(nullable = false)
    private String description;
    @Size(max = 100)
    @NotNull
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    @NotNull
    private Point coordinates;
    @Min(0)
    private int roomCount;
    @DecimalMin(value = "0")
    @DecimalMax(value = "9999999.99")
    @NotNull
    @Column(precision = 9, scale = 2, nullable = false)
    private BigDecimal square;
    @DecimalMin(value = "0")
    @DecimalMax(value = "999999999.99")
    @NotNull
    @Column(precision = 11, scale = 2, nullable = false)
    private BigDecimal cost;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private UserEntity creator;
    @NotNull
    @ToString.Exclude
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<MediaEntity> medias;
}
