package space.meduzza.property.util;

import space.meduzza.property.model.MediaEntity;
import space.meduzza.property.model.PropertyEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class MediaTestUtil {

    public static MediaEntity createInstance(
            final long id,
            final PropertyEntity propertyEntity
    ) {
        MediaEntity mediaEntity = new MediaEntity(propertyEntity, "test" + id);
        mediaEntity.setId(id);
        mediaEntity.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        mediaEntity.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
        return mediaEntity;
    }
}
