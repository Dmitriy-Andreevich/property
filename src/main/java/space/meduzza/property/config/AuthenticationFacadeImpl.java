package space.meduzza.property.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import space.meduzza.property.model.UserEntity;
import space.meduzza.property.service.user.UserService;

import java.util.NoSuchElementException;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {
    private final UserService userService;

    public AuthenticationFacadeImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public UserEntity findCurrentUser() throws NoSuchElementException {
        return userService.findUserByEmail(getCurrentUserName()).orElseThrow();
    }
}
