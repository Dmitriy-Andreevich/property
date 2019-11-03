package space.meduzza.property.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import space.meduzza.property.model.PropertyEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class PropertyTestUtil {

    public static PropertyEntity createInstance(final long id) {
        final PropertyEntity propertyEntity = PropertyEntity
                .builder()
                .title("test")
                .address("test")
                .coordinates(new GeometryFactory().createPoint(new Coordinate(1, 1)))
                .description("test")
                .cost(BigDecimal.ONE)
                .roomCount(5)
                .square(BigDecimal.ONE)
                .build();
        propertyEntity.setId(id);
        propertyEntity.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        propertyEntity.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
        return propertyEntity;
    }
}
