package space.meduzza.property.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import space.meduzza.property.model.UserEntity;
import space.meduzza.property.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    private UserEntity user;

    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
        @Bean
        public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }
    }

    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userServiceImpl;


    @BeforeEach
    void setUp() {
        user = new UserEntity()
                .toBuilder()
                .email("test@email.com")
                .authorities("ROLE_USER")
                .password("test")
                .build();
        user.setId(1);

    }

    @Test
    void loadUserByUsername() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThat(userServiceImpl.loadUserByUsername(user.getEmail()))
                .isNotNull();
    }

    @Test
    void createUser() {
        when(userRepository.save(any(UserEntity.class))).then(e->e.getArgument(0));
        UserEntity test = userServiceImpl.createUser(user.getEmail(), "test");
        assertThat(test).isNotNull();
    }

    @Test
    void createUserIfNotExist() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        userServiceImpl.createUserIfNotExist(user.getEmail());
        verify(userRepository, never()).save(any());
    }

    @Test
    void findUserById() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        assertThat(userServiceImpl.findUserById(user.getId()))
                .isNotNull();
    }

    @Test
    void findUserByEmail() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        assertThat(userServiceImpl.findUserByEmail(user.getEmail()))
                .isNotNull();
    }
}