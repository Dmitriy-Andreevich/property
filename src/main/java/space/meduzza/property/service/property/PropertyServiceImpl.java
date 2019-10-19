package space.meduzza.property.service.property;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import space.meduzza.property.model.PropertyEntity;
import space.meduzza.property.repository.PropertyRepository;

import java.util.List;

@Service
public class PropertyServiceImpl implements PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public PropertyEntity createProperty(PropertyEntity entity) {
        return propertyRepository.save(entity);
    }

    @Override
    public PropertyEntity getPropertyById(int id) {
        return propertyRepository.findById(id).orElseThrow();
    }

    @Override
    public List<PropertyEntity> getAllPropertiesByCoordinates(float latitude, float longitude, int radius, int page) {
        return propertyRepository.findAllPropertyInRange(longitude, latitude, radius, PageRequest.of(page, 5));
    }

    @Override
    public void deletePropertyById(int id) {
        propertyRepository.deleteById(id);
    }
}
