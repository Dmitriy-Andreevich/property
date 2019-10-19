package space.meduzza.property;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import space.meduzza.property.service.user.UserService;
import space.meduzza.property.service.user.UserServiceImpl;

@SpringBootApplication
public class PropertyApplication implements CommandLineRunner {
    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(PropertyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        userService.createUser("donsy2002@gmail.com", "qwe565656", "Dmitriy", "Andreevich");
    }
}
