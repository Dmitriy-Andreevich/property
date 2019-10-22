package space.meduzza.property.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import space.meduzza.property.model.UserEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity()
                .toBuilder()
                .email("test@email.com")
                .authorities("ROLE_USER")
                .password("test")
                .build();
    }

    @Test
    void findByEmail() {
        entityManager.persist(user);
        entityManager.flush();

        UserEntity found = userRepository.findByEmail(user.getEmail()).get();
        assertThat(found.getPassword())
                .isEqualTo(user.getPassword());
    }
}