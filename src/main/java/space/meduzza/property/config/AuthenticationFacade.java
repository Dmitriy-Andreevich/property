package space.meduzza.property.config;

import org.springframework.security.core.Authentication;
import space.meduzza.property.model.UserEntity;

import java.util.NoSuchElementException;

public interface AuthenticationFacade {
    Authentication getAuthentication();
    String getCurrentUserName();
    UserEntity findCurrentUser() throws NoSuchElementException;
}
