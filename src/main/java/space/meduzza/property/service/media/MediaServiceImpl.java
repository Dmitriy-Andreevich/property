package space.meduzza.property.service.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.storage.GoogleStorageResource;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.WritableResource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import space.meduzza.property.config.AuthenticationFacade;
import space.meduzza.property.config.AuthenticationFacadeImpl;
import space.meduzza.property.model.MediaEntity;
import space.meduzza.property.model.PropertyEntity;
import space.meduzza.property.repository.MediaRepository;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MediaServiceImpl implements MediaService {
    @Value("${gcs.bucket-name}")
    private final String BUCKET_NAME = "property-bucket-983";
    @Value("${gcs.media-folder-name}")
    private final String FOLDER_NAME = "media";

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private AuthenticationFacade authenticationFacade;


    @Override
    @Transactional
    public List<MediaEntity> attachMedia(PropertyEntity property, List<byte[]> medias) {
        if(medias.isEmpty()) return Collections.emptyList();
        return mediaRepository.saveAll(
                medias.stream().map(el -> {
                    try {
                        String extension = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(el));
                        String name = UUID.randomUUID() + "." +extension.substring(extension.lastIndexOf('/')+1);
                        try (OutputStream os = ((WritableResource) applicationContext.getResource(getMediaLink(name))).getOutputStream()) {
                            os.write(el);
                            return new MediaEntity(property, name);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    throw new RuntimeException();
                }).collect(Collectors.toList()));
    }

    @Override
    public List<MediaEntity> getAllMediaByProperty(long propertyId) {
        return mediaRepository.findAllByPropertyId(propertyId);
    }

    @Override
    public List<String> getAllMediaLinksByProperty(long propertyId) {
        return getAllMediaByProperty(propertyId).stream().map(el -> getMediaLink(el.getName())).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteMediaByPropertyId(long mediaId) {
        final MediaEntity mediaEntities = mediaRepository.findById(mediaId).orElseThrow();
        final long resourceOwnerId = mediaEntities.getProperty().getCreator().getId();
        authenticationFacade.isOwnerResourceWithException(resourceOwnerId);
        mediaRepository.delete(mediaEntities);
        try {
            ((GoogleStorageResource) applicationContext.getResource(getMediaLink(mediaEntities.getName()))).getBlob().delete();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void deleteAllMediaByProperty(long propertyId) {
        final List<MediaEntity> mediaEntities = mediaRepository.findAllByPropertyId(propertyId);
        if(mediaEntities.isEmpty())
            return;
        final long resourceOwnerId = mediaEntities.get(0).getProperty().getCreator().getId();
        authenticationFacade.isOwnerResourceWithException(resourceOwnerId);
        mediaRepository.deleteAllByPropertyId(propertyId);
        mediaEntities.forEach((el -> {
            try {
                ((GoogleStorageResource) applicationContext.getResource(getMediaLink(el.getName()))).getBlob().delete();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }));
    }

    @Override
    public String getMediaLink(String name) {
        return "gs://" + BUCKET_NAME + "/" + FOLDER_NAME + "/" + name;
    }
}
