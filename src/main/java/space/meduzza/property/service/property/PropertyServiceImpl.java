package space.meduzza.property.service.property;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import space.meduzza.property.config.AuthenticationFacade;
import space.meduzza.property.model.PropertyEntity;
import space.meduzza.property.repository.PropertyRepository;
import space.meduzza.property.service.media.MediaService;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PropertyServiceImpl implements PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    @Transactional
    public PropertyEntity createProperty(PropertyEntity entity, List<byte[]> medias) {
        PropertyEntity property = propertyRepository.save(entity);
        mediaService.attachMedia(property, medias);
        return property;
    }

    @Override
    public PropertyEntity updateProperty(long id, PropertyEntity entity) {
        final PropertyEntity originalEntity = findPropertyById(id).orElseThrow();
        final PropertyEntity updatedEntity = originalEntity
                .setTitle(entity.getTitle())
                .setDescription(entity.getDescription())
                .setAddress(entity.getAddress())
                .setCoordinates(entity.getCoordinates())
                .setRoomCount(entity.getRoomCount())
                .setSquare(entity.getSquare())
                .setCost(entity.getCost());
        return propertyRepository.save(updatedEntity);
    }

    @Override
    public Optional<PropertyEntity> findPropertyById(long id) {
        return propertyRepository.findById(id);
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
    public Page<PropertyEntity> getAllPropertiesByCoordinates(BigDecimal latitude, BigDecimal longitude, int radius, int page) {
        return propertyRepository.findAllPropertyInRange(longitude, latitude, radius, PageRequest.of(page, 5));
    }

    @Override
    @Transactional
    public void deletePropertyById(long id) {
        mediaService.deleteAllMediaByProperty(id);
        propertyRepository.deleteById(id);
    }
}
