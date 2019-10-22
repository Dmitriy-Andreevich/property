package space.meduzza.property.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import space.meduzza.property.model.UserEntity;
import space.meduzza.property.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return new User(userEntity.getEmail(), userEntity.getPassword(),
                Arrays.stream(
                        userEntity
                                .getAuthorities()
                                .split(",")
                ).map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
    }

    @Override
    public UserEntity createUser(String email, String password) {
        return userRepository.save(
                new UserEntity(email, passwordEncoder.encode(password), "ROLE_USER", List.of())
        );
    }

    @Override
    public UserEntity createUserIfNotExist(String email) {
        return findUserByEmail(email).orElseGet(() -> createUser(email, randomPassword()));
    }

    @Override
    public Optional<UserEntity> findUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserEntity> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private String randomPassword(){
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toLowerCase();
    }
}
