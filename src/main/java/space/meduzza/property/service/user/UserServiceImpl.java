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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(s).orElseThrow(() -> new UsernameNotFoundException(s));
        return new User(userEntity.getEmail(), userEntity.getPassword(),
                Arrays.stream(
                        userEntity
                                .getAuthorities()
                                .split(",")
                ).map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
    }

    @Override
    public UserEntity createUser(String email, String password, String fname, String lname) {
        return userRepository.save(
                new UserEntity(fname, lname, email, passwordEncoder.encode(password), "", List.of())
        );
    }

    @Override
    public Optional<UserEntity> findUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserEntity> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}
