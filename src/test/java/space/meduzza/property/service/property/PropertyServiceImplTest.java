package space.meduzza.property.service.property;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import space.meduzza.property.config.AuthenticationFacade;
import space.meduzza.property.model.PropertyEntity;
import space.meduzza.property.model.UserEntity;
import space.meduzza.property.repository.PropertyRepository;
import space.meduzza.property.service.media.MediaService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class PropertyServiceImplTest {

    private PropertyEntity property;

    @TestConfiguration
    static class PropertyServiceImplTestContextConfiguration {
        @Bean
        public PropertyService propertyService() {
            return new PropertyServiceImpl();
        }
    }

    @Autowired
    private PropertyService propertyService;
    @MockBean
    private PropertyRepository propertyRepository;
    @MockBean
    private MediaService mediaService;
    @MockBean
    private AuthenticationFacade authenticationFacade;

    @BeforeEach
    public void setup() {
        final UserEntity userEntity = new UserEntity()
                .toBuilder()
                .email("test@email.com")
                .authorities("ROLE_USER")
                .password("test")
                .build();

        property = PropertyEntity.builder()
                .title("test")
                .address("test")
                .coordinates(new GeometryFactory().createPoint(new Coordinate(1, 1)))
                .description("test")
                .cost(BigDecimal.ONE)
                .roomCount(5)
                .creator(userEntity)
                .square(BigDecimal.ONE)
                .build();

        when(authenticationFacade.findCurrentUser()).thenReturn(userEntity);
    }

    @Test
    void createProperty() {
        when(propertyRepository.save(property)).then(i -> i.getArgument(0));
        when(mediaService.attachMedia(any(), any())).thenReturn(emptyList());
        PropertyEntity property = propertyService.createProperty(this.property, emptyList());
        assertThat(property)
                .isNotNull();
    }

    @Test
    void updateProperty() {
        long id = 1;
        String oldTitle = property.getTitle();
        String newTitle = "a";
        when(propertyRepository.save(property)).then(i -> i.getArgument(0));//возвращает то что пришло на вход но не null
        when(propertyRepository.findById(id)).thenReturn(Optional.of(property));
        PropertyEntity actual = propertyService.updateProperty(id, property.setTitle(newTitle));
        assertThat(actual)
                .isNotNull();
        assertNotEquals(actual.getTitle(), oldTitle);
        property.setTitle(oldTitle);
    }

    @Test
    void findPropertyById() {
        long id = 1;
        when(propertyRepository.findById(id)).thenReturn(Optional.of(property));
        assertThat(propertyService.findPropertyById(id))
                .isNotNull();
    }


    @Test
    void findAllProperty() {
        when(propertyRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(property)));
        assertThat(propertyService.findAllProperty(1))
                .isNotNull()
                .hasSize(1);
    }

    @Test
    void getAllPropertiesByCoordinates() {
        when(propertyRepository.findAllPropertyInRange(any(BigDecimal.class),any(BigDecimal.class),anyInt(), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(property)));
        assertThat(propertyService.getAllPropertiesByCoordinates(BigDecimal.ONE, BigDecimal.ONE, 10, 1))
                .isNotNull()
                .hasSize(1);
    }

    @Test
    void deletePropertyById() {
        when(propertyRepository.findById(any())).thenReturn(Optional.of(property));
        propertyService.deletePropertyById(0);
        verify(propertyRepository).deleteById(any());
    }
}