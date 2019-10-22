package space.meduzza.property.service.media;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import space.meduzza.property.model.MediaEntity;
import space.meduzza.property.model.PropertyEntity;
import space.meduzza.property.repository.MediaRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
//@DataJpaTest
@AutoConfigureTestDatabase
class MediaServiceImplTest {
    @Autowired
    private MediaService mediaService;

    @BeforeEach
    void setUp() {
    }


    @Test
    void test() {
        List<MediaEntity> media = mediaService.attachMedia(PropertyEntity.builder()
                .title("test")
                .address("test")
                .coordinates(new GeometryFactory().createPoint(new Coordinate(1, 1)))
                .description("test")
                .cost(BigDecimal.ONE)
                .roomCount(5)
                .square(BigDecimal.ONE)
                .build(), List.of("test".getBytes()));

        System.out.println(mediaService.getMediaLink(media.get(0).getName()));
    }
}