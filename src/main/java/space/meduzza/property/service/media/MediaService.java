package space.meduzza.property.service.media;

import space.meduzza.property.model.MediaEntity;
import space.meduzza.property.model.PropertyEntity;

import java.util.List;

public interface MediaService {
    List<MediaEntity> attachMedia(PropertyEntity property, List<byte[]> medias);
    List<MediaEntity> getAllMediaByProperty(long propertyId);
    List<String> getAllMediaLinksByProperty(long propertyId);
    void deleteMediaByPropertyId(long propertyId);
    void deleteAllMediaByProperty(long propertyId);
    String getMediaLink(String name);
}
