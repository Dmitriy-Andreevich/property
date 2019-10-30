package space.meduzza.property.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Collections;

@Component
class CustomSignInAdapter implements SignInAdapter {
    public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                connection.getDisplayName() != null ? connection.getDisplayName() : connection.fetchUserProfile().getEmail(),
                null,
                Collections.singletonList(new SimpleGrantedAuthority(connection.getKey().getProviderId().toUpperCase()))
        ));
        return "/";
    }
}