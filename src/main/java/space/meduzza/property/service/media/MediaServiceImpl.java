package space.meduzza.property.service.media;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.storage.GoogleStorageResource;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Service;
import space.meduzza.property.config.AuthenticationFacade;
import space.meduzza.property.error.MediaFileUploadException;
import space.meduzza.property.model.MediaEntity;
import space.meduzza.property.model.PropertyEntity;
import space.meduzza.property.repository.MediaRepository;

import javax.transaction.Transactional;
import javax.validation.constraints.Min;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MediaServiceImpl implements MediaService {
    @Value("${gcs.bucket-name}")
    private final String BUCKET_NAME = "property-bucket-983";

    @Value("${gcs.media-folder-name}")
    private final String FOLDER_NAME = "media";

    private final ApplicationContext applicationContext;

    private final MediaRepository mediaRepository;

    private final AuthenticationFacade authenticationFacade;

    public MediaServiceImpl(
            ApplicationContext applicationContext,
            MediaRepository mediaRepository,
            AuthenticationFacade authenticationFacade
    ) {
        this.applicationContext = applicationContext;
        this.mediaRepository = mediaRepository;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    @Transactional
    public List<MediaEntity> attachMedia(
            final PropertyEntity property,
            final List<byte[]> medias
    ) {
        if (medias.isEmpty()) {
            return Collections.emptyList();
        }
        return mediaRepository.saveAll(medias
                                               .stream()
                                               .map((element) -> {
                                                   final String name = generateFileNameWithExtensions(element);
                                                   try (OutputStream os = ((WritableResource) applicationContext.getResource(
                                                           getMediaLink(name))).getOutputStream()) {
                                                       os.write(element);
                                                       return new MediaEntity(property, name);
                                                   } catch (IOException e) {
                                                       throw new MediaFileUploadException("File upload exception");
                                                   }
                                               })
                                               .collect(Collectors.toList()));
    }

    private String generateFileNameWithExtensions(final byte[] el) {
        try {
            String extension = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(el));
            return joinRandomFileNameWithExtension(extension);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String joinRandomFileNameWithExtension(String extension) {
        return getRandomFileName() + "." + getFileExtensionFromMime(extension);
    }

    private UUID getRandomFileName() {
        return UUID.randomUUID();
    }

    private String getFileExtensionFromMime(String extension) {
        return extension.substring(extension.lastIndexOf('/') + 1);
    }

    @Override
    public List<MediaEntity> getAllMediaByProperty(final long propertyId) {
        return mediaRepository.findAllByPropertyId(propertyId);
    }

    @Override
    public List<String> getAllMediaLinksByProperty(final long propertyId) {
        return getAllMediaByProperty(propertyId)
                .stream()
                .map(el -> getMediaLink(el.getName()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteMediaById(final long id) {
        final MediaEntity mediaEntities = mediaRepository
                .findById(id)
                .orElseThrow();
        final long resourceOwnerId = getMediaEntityCreatorId(mediaEntities);
        authenticationFacade.isOwnerResourceWithException(resourceOwnerId);
        mediaRepository.delete(mediaEntities);
        try {
            ((GoogleStorageResource) applicationContext.getResource(getMediaLink(mediaEntities.getName())))
                    .getBlob()
                    .delete();
        } catch (IOException | NullPointerException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Min(0)
    private long getMediaEntityCreatorId(MediaEntity mediaEntities) {
        return mediaEntities
                .getProperty()
                .getCreator()
                .getId();
    }

    @Override
    @Transactional
    public void deleteAllMediaByProperty(final long propertyId) {
        final List<MediaEntity> mediaEntities = mediaRepository.findAllByPropertyId(propertyId);
        if (mediaEntities.isEmpty()) {
            return;
        }
        final long resourceOwnerId = getMediaEntityCreatorId(mediaEntities.get(0));
        authenticationFacade.isOwnerResourceWithException(resourceOwnerId);
        mediaRepository.deleteAllByPropertyId(propertyId);
        mediaEntities.forEach((el -> {
            try {
                ((GoogleStorageResource) applicationContext.getResource(getMediaLink(el.getName())))
                        .getBlob()
                        .delete();
            } catch (IOException | NullPointerException e) {
                log.error(e.getMessage(), e);
            }
        }));
    }

    @Override
    public String getMediaLink(final String name) {
        return "gs://" + BUCKET_NAME + "/" + FOLDER_NAME + "/" + name;
    }
}
