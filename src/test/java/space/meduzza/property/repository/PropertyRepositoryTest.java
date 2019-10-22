package space.meduzza.property.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import space.meduzza.property.model.PropertyEntity;
import space.meduzza.property.model.UserEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PropertyRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private PropertyRepository propertyRepository;
    private UserEntity creator;

    @BeforeEach
    void setUp() {
        creator = new UserEntity()
                .toBuilder()
                .email("test@email.com")
                .authorities("ROLE_USER")
                .password("test")
                .build();
        em.persist(creator);
        em.persist(PropertyEntity.builder()
                .title("test1")
                .address("test")
                .coordinates(new GeometryFactory().createPoint(new Coordinate(10, 10)))
                .description("test")
                .cost(BigDecimal.ONE)
                .roomCount(5)
                .medias(Collections.emptyList())
                .square(BigDecimal.ONE)
                .creator(creator)
                .build());
        em.persist(PropertyEntity.builder()
                .title("test2")
                .address("test")
                .coordinates(new GeometryFactory().createPoint(new Coordinate(10.5, 10.5)))
                .description("test")
                .cost(BigDecimal.ONE)
                .roomCount(5)
                .square(BigDecimal.ONE)
                .medias(Collections.emptyList())
                .creator(creator)
                .build());
        em.persist(PropertyEntity.builder()
                .title("test3")
                .address("test")
                .coordinates(new GeometryFactory().createPoint(new Coordinate(11, 11)))
                .description("test")
                .cost(BigDecimal.ONE)
                .medias(Collections.emptyList())
                .roomCount(5)
                .square(BigDecimal.ONE)
                .build());

    }

    @Test
    void findAllByCreatorId() {
        assertThat(propertyRepository.findAllByCreatorId(creator.getId(), PageRequest.of(1, 5)))
                .hasSize(2);
    }

}