package space.meduzza.property.service.property;

import org.springframework.data.domain.Page;
import space.meduzza.property.model.PropertyEntity;

public interface PropertyService {
    PropertyEntity createProperty(PropertyEntity entity);
    PropertyEntity getPropertyById(int id);
    Page<PropertyEntity> getAllPropertiesByCoordinates(float latitude, float longitude, int radius, int page);
    void deletePropertyById(int id);
}
