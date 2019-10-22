package space.meduzza.property.service.property;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import space.meduzza.property.config.AuthenticationFacade;
import space.meduzza.property.model.PropertyEntity;
import space.meduzza.property.repository.PropertyRepository;

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
    public Page<PropertyEntity> findAllUserProperty(int page){
        return propertyRepository.findAllByCreatorId(authenticationFacade.findCurrentUser().getId(), PageRequest.of(page, 10));
    }

    @Override
    public Page<PropertyEntity> findAllProperty(int page){
        return propertyRepository.findAll(PageRequest.of(page, 10));
    }

    @Override
    public Page<PropertyEntity> getAllPropertiesByCoordinates(float latitude, float longitude, int radius, int page) {
        return propertyRepository.findAllPropertyInRange(longitude, latitude, radius, PageRequest.of(page, 5));
    }

    @Override
    public void deletePropertyById(int id) {
        propertyRepository.deleteById(id);
    }
}
