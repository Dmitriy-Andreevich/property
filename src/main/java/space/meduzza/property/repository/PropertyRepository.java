package space.meduzza.property.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import space.meduzza.property.model.PropertyEntity;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, Long> {
    Page<PropertyEntity> findAllByCreatorId(long creatorId, Pageable pageable);
    @Query(value="SELECT * FROM properties WHERE ST_DWithin(cast(properties.coordinates as geography),ST_SetSRID(ST_Point(?2, ?1),4326), ?3) ORDER BY ST_Distance(cast(properties.coordinates as geography), ST_Point(?2, ?1))", nativeQuery = true)
    Page<PropertyEntity> findAllPropertyInRange(BigDecimal longitude, BigDecimal latitude, int radius, Pageable pageable);
}