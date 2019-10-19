package space.meduzza.property.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import space.meduzza.property.model.PropertyEntity;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, Integer> {
    @Query(value="SELECT * FROM properties WHERE ST_DWithin(cast(properties.coordinates as geography),ST_SetSRID(ST_Point(?2, ?1),4326), ?3)", nativeQuery = true)
    List<PropertyEntity> findAllPropertyInRange(double longitude, double latitude, int radius, Pageable pageable);
}