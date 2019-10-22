package space.meduzza.property.service.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import space.meduzza.property.model.UserEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface UserService extends UserDetailsService {
    UserEntity createUser(String email, String password);
    UserEntity createUserIfNotExist(String email);
    Optional<UserEntity> findUserById(long id);
    Optional<UserEntity> findUserByEmail(String email);

    static List<SimpleGrantedAuthority> getAuthorities(UserEntity userEntity) {
        return Arrays.stream(
                userEntity
                        .getAuthorities()
                        .split(",")
        ).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
