package space.meduzza.property.service.property;

import space.meduzza.property.model.PropertyEntity;

import java.util.List;
import java.util.Set;

public interface PropertyService {
    PropertyEntity createProperty(PropertyEntity entity);
    PropertyEntity getPropertyById(int id);
    List<PropertyEntity> getAllPropertiesByCoordinates(float latitude, float longitude, int radius, int page);
    void deletePropertyById(int id);
}
