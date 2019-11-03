package space.meduzza.property.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import space.meduzza.property.model.UserEntity;
import space.meduzza.property.util.UserTestUtil;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private UserEntity newUser;

    private UserEntity secondNewUser;

    private UserEntity persistentUser;

    @BeforeEach
    void setUp() {
        newUser = UserTestUtil.createInstanceWithNewState();
        secondNewUser = UserTestUtil.createInstanceWithNewState();
        persistentUser = UserTestUtil.createInstanceWithPersistentState(1);
    }

    @Test
    void create_UserEntity_ReturnOkey() {
        final UserEntity savedUser = userRepository.save(newUser);
        assertThat(savedUser.getId()).isGreaterThan(0);
        assertThat(savedUser.getEmail()).isNotNull();
        assertThat(savedUser.getCreateTime()).isNotNull();
        assertThat(savedUser.getUpdateTime()).isNotNull();
        assertThat(userRepository.findById(savedUser.getId())).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"test.email.com", "test.test", "@", "email.tesd@.dsa", "@.@", ".@."})
    void create_UserEntityWithNoValidEmail_ReturnThrow(final String email) {
        newUser.setEmail(email);
        assertThrows(ConstraintViolationException.class, () -> userRepository.save(newUser));
    }

    @Test
    void create_TwoUserEntities_ReturnOkey() {
        final List<UserEntity> savedUsers = userRepository.saveAll(List.of(newUser, newUser));
        savedUsers.forEach((persistUser) -> {
            assertThat(persistUser.getId()).isGreaterThan(0);
            assertThat(persistUser.getEmail()).isNotNull();
            assertThat(persistUser.getCreateTime()).isNotNull();
            assertThat(persistUser.getUpdateTime()).isNotNull();
            assertThat(userRepository.findById(persistUser.getId())).isNotNull();
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"test.email.com", "test.test", "@", "email.tesd@.dsa", "@.@", ".@."})
    void create_TwoUserEntitiesWithNoValidEmail_ReturnFirstOkeySecondError(final String email) {
        newUser.setEmail("validEmail@mail.com");
        secondNewUser.setEmail(email);
//        userRepository.saveAll(List.of(newUser, secondNewUser));
        assertThrows(Exception.class, () -> userRepository.saveAll(List.of(newUser, secondNewUser)));
        assertThat(userRepository.findAll()).hasSize(2);
    }

    void update_UserEntity_ReturnOkey() {

    }

//    @Test
//    void find_UserEntityByEmail_ReturnOkey() {
//        userRepository.save(user);
//
//        UserEntity found = userRepository
//                .findByEmail(user.getEmail())
//                .get();
//        assertThat(found.getPassword()).isEqualTo(user.getPassword());
//    }
}