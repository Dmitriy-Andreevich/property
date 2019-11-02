package space.meduzza.property.service.property;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import space.meduzza.property.model.PropertyEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PropertyService {
    PropertyEntity createProperty(
            PropertyEntity inputProperty,
            List<byte[]> medias
    ) throws FileUploadException;

    PropertyEntity updateProperty(
            long id,
            PropertyEntity entity
    );

    Optional<PropertyEntity> findPropertyById(long id);

    Page<PropertyEntity> findAllUserProperty(int page);

    Page<PropertyEntity> findAllProperty(int page);

    Page<PropertyEntity> getAllPropertiesByCoordinates(
            BigDecimal latitude,
            BigDecimal longitude,
            int radius,
            int page
    );

    void deletePropertyById(long id);
}
