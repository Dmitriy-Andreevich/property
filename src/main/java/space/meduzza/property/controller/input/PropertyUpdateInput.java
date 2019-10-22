package space.meduzza.property.controller.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import space.meduzza.property.model.PropertyEntity;
import space.meduzza.property.model.UserEntity;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class PropertyUpdateInput {
    @Size(min = 3, max = 100)
    String title;
    @Size(max = 10000)
    String description;
    @Size(max = 100)
    String address;
    @DecimalMin("-90")
    @DecimalMax("90")
    BigDecimal latitude;
    @DecimalMin("-180")
    @DecimalMax("180")
    BigDecimal longitude;
    @Min(1)
    int roomCount;
    @DecimalMin(value = "0")
    @DecimalMax(value = "9999999.99")
    BigDecimal square;
    @DecimalMin(value = "0")
    @DecimalMax(value = "999999999.99")
    BigDecimal cost;

    public PropertyEntity toPropertyEntity(UserEntity userEntity){
        return new PropertyEntity(
                title,
                description,
                address,
                new GeometryFactory().createPoint(new Coordinate(latitude.doubleValue(), longitude.doubleValue())),
                roomCount,
                square,
                cost,
                userEntity,
                List.of()
        );
    }
}