package space.meduzza.property.service.media;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import space.meduzza.property.model.MediaEntity;
import space.meduzza.property.model.PropertyEntity;
import space.meduzza.property.repository.MediaRepository;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
//@DataJpaTest
@AutoConfigureTestDatabase
class MediaServiceImplTest {

    private PropertyEntity property;

    @TestConfiguration
    static class MediaServiceImplTestContextConfiguration {
        @Bean
        public MediaService mediaService() {
            return new MediaServiceImpl();
        }
    }

    @Autowired
    private MediaService mediaService;

    @MockBean
    private MediaRepository mediaRepository;

    @Value("classpath:test.jpeg")
    private Resource resource;

    @Autowired
    private ResourceLoader loader;

    @BeforeEach
    void setUp() {
        property = PropertyEntity
                .builder()
                .title("test")
                .address("test")
                .coordinates(new GeometryFactory().createPoint(new Coordinate(1, 1)))
                .description("test")
                .cost(BigDecimal.ONE)
                .roomCount(5)
                .square(BigDecimal.ONE)
                .build();

    }


    @Test
    void writeAndDeleteTest() throws IOException {
        when(mediaRepository.saveAll(any())).then(e -> e.getArgument(0));
        InputStream inputStream = resource.getInputStream();
        byte[] e1 = inputStream.readAllBytes();
        inputStream.close();
        List<MediaEntity> media = mediaService.attachMedia(property, List.of(e1));

        MediaEntity mediaEntity = media.get(0);
        assertThat(loader
                           .getResource(mediaService.getMediaLink(mediaEntity.getName()))
                           .getInputStream()
                           .readAllBytes()).isEqualTo(e1);
        when(mediaRepository.findAllByPropertyId(property.getId())).thenReturn(List.of(mediaEntity));
        mediaService.deleteAllMediaByProperty(property.getId());
        assertThat(loader
                           .getResource(mediaService.getMediaLink(mediaEntity.getName()))
                           .exists()).isFalse();

    }

}