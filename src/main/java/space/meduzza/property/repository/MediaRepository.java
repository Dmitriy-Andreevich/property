package space.meduzza.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.meduzza.property.model.MediaEntity;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<MediaEntity, Long> {
    List<MediaEntity> findAllByPropertyId(long propertyId);

    void deleteAllByPropertyId(final long propertyId);
}
