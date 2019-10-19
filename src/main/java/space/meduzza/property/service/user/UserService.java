package space.meduzza.property.service.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import space.meduzza.property.model.UserEntity;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    UserEntity createUser(String email, String password, String fname, String lname);
    Optional<UserEntity> findUserById(int id);
    Optional<UserEntity> findUserByEmail(String email);
}
