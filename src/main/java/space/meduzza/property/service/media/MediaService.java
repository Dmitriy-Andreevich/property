package space.meduzza.property.service.media;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.validation.annotation.Validated;
import space.meduzza.property.model.MediaEntity;
import space.meduzza.property.model.PropertyEntity;

import java.util.List;

@Validated
public interface MediaService {
    List<MediaEntity> attachMedia(
            final PropertyEntity property,
            final List<byte[]> medias
    ) throws FileUploadException;

    List<MediaEntity> getAllMediaByProperty(final long propertyId);

    List<String> getAllMediaLinksByProperty(final long propertyId);

    void deleteMediaById(final long propertyId);

    void deleteAllMediaByProperty(final long propertyId);

    String getMediaLink(final String name);
}
