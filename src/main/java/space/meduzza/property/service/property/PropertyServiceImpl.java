package space.meduzza.property.service.property;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import space.meduzza.property.config.AuthenticationFacade;
import space.meduzza.property.model.PropertyEntity;
import space.meduzza.property.repository.PropertyRepository;
import space.meduzza.property.service.media.MediaService;

import javax.transaction.Transactional;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;

    private final MediaService mediaService;

    private final AuthenticationFacade authenticationFacade;

    public PropertyServiceImpl(
            final PropertyRepository propertyRepository,
            final MediaService mediaService,
            final AuthenticationFacade authenticationFacade
    ) {
        this.propertyRepository = propertyRepository;
        this.mediaService = mediaService;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    @Transactional
    public PropertyEntity createProperty(
            final PropertyEntity entity,
            final List<byte[]> medias
    ) throws FileUploadException {
        PropertyEntity property = propertyRepository.save(entity);
        mediaService.attachMedia(property, medias);
        return property;
    }

    @Override
    public PropertyEntity updateProperty(
            final long id,
            final PropertyEntity inputEntity
    ) {
        final PropertyEntity originalEntity = findPropertyById(id).orElseThrow();
        authenticationFacade.isOwnerResourceWithException(getClientIdFromPropertyEntity(originalEntity));
        return propertyRepository.save(buildUpdatedEntity(originalEntity, inputEntity));
    }

    private PropertyEntity buildUpdatedEntity(
            PropertyEntity originalEntity,
            PropertyEntity entity
    ) {
        return originalEntity
                .setTitle(entity.getTitle())
                .setDescription(entity.getDescription())
                .setAddress(entity.getAddress())
                .setCoordinates(entity.getCoordinates())
                .setRoomCount(entity.getRoomCount())
                .setSquare(entity.getSquare())
                .setCost(entity.getCost());
    }

    @Override
    public Optional<PropertyEntity> findPropertyById(final long id) {
        return propertyRepository.findById(id);
    }

    @Override
    public Page<PropertyEntity> findAllUserProperty(final int page) {
        return propertyRepository.findAllByCreatorId(authenticationFacade
                                                             .findCurrentUser()
                                                             .getId(), PageRequest.of(page, 10));
    }

    @Override
    public Page<PropertyEntity> findAllProperty(final int page) {
        return propertyRepository.findAll(PageRequest.of(page, 10));
    }

    @Override
    public Page<PropertyEntity> getAllPropertiesByCoordinates(
            final BigDecimal latitude,
            final BigDecimal longitude,
            final int radius,
            final int page
    ) {
        return propertyRepository.findAllPropertyInRange(longitude, latitude, radius, PageRequest.of(page, 5));
    }

    @Override
    @Transactional
    public void deletePropertyById(final long id) {
        final PropertyEntity propertyEntity = findPropertyById(id).orElse(null);
        if (propertyEntity == null) {
            return;
        }
        authenticationFacade.isOwnerResourceWithException(getClientIdFromPropertyEntity(propertyEntity));
        mediaService.deleteAllMediaByProperty(id);
        propertyRepository.deleteById(id);
    }

    @Min(0)
    private long getClientIdFromPropertyEntity(final PropertyEntity originalEntity) {
        return originalEntity
                .getCreator()
                .getId();
    }
}
