package space.meduzza.property.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.github.api.GitHub;
import org.springframework.stereotype.Component;
import space.meduzza.property.model.UserEntity;
import space.meduzza.property.service.user.UserService;

import java.util.List;
import java.util.Map;

@Component
public class SignUpAdapter implements ConnectionSignUp {
    private final UserService userService;

    public SignUpAdapter(UserService userService) {
        this.userService = userService;
    }

    public String execute(Connection<?> connection){
        UserEntity userEntity;
        switch (connection.getKey().getProviderId()){
            case "facebook":
                User user = ((Facebook)connection.getApi()).fetchObject("me", User.class, "id", "email", "first_name", "last_name");
                userEntity = userService.createUserIfNotExist(user.getEmail());
                break;
            case "github":
                String emaill = ((Map<String, String>) ((GitHub) connection.getApi()).restOperations().getForObject("https://api.github.com/user/emails", List.class).get(0)).get("email");
                userEntity = userService.createUserIfNotExist(emaill);
                break;
            default:
                UserProfile profile = connection.fetchUserProfile();
                userEntity = userService.createUserIfNotExist(profile.getEmail());
            }

        return String.valueOf(userEntity.getId());
    }
}
