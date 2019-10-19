package space.meduzza.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.meduzza.property.model.MediaEntity;

@Repository
public interface MediaRepository extends JpaRepository<MediaEntity, Integer> { }
