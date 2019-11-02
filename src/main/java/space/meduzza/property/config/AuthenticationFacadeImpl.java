package space.meduzza.property.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import space.meduzza.property.model.UserEntity;
import space.meduzza.property.service.user.UserService;

import javax.validation.constraints.NotNull;
import java.util.NoSuchElementException;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {
    private final UserService userService;

    public AuthenticationFacadeImpl(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication();
    }

    @Override
    public String getCurrentUserName() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    @Override
    public UserEntity findCurrentUser() throws NoSuchElementException {
        return userService
                .findUserByEmail(getCurrentUserName())
                .orElseThrow();
    }

    @Override
    public boolean isOwnerResource(final long resourceOwnerId) {
        return findCurrentUser().getId() == resourceOwnerId;
    }

    @Override
    public void isOwnerResourceWithException(
            final long resourceOwnerId,
            @NotNull
            final String errorMessage
    ) throws AccessDeniedException {
        if (isOwnerResource(resourceOwnerId)) {
            throw new AccessDeniedException(errorMessage);
        }
    }

    @Override
    public void isOwnerResourceWithException(final long resourceOwnerId) throws AccessDeniedException {
        isOwnerResourceWithException(resourceOwnerId, "Access Denied!");
    }
}
