package space.meduzza.property.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.github.connect.GitHubConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import space.meduzza.property.service.user.UserService;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${facebook.appId}")
    private String facebookId;

    @Value("${facebook.appSecret}")
    private String facebookSecret;

    @Value("${google.appId}")
    private String googleId;

    @Value("${google.appSecret}")
    private String googleSecret;

    @Value("${github.appId}")
    private String githubId;

    @Value("${github.appSecret}")
    private String githubSecret;


    private final SignUpAdapter signUpAdapter;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfig(
            final SignUpAdapter signUpAdapter,
            final UserService userService,
            final PasswordEncoder passwordEncoder
    ) {
        this.signUpAdapter = signUpAdapter;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/login*",
                             "/signin/**",
                             "/signup/**",
                             "/images/**",
                             "/vendor/**",
                             "/fonts/**",
                             "/css/**",
                             "/js/**",
                             "/error")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll();
    }

    @Bean
    public ProviderSignInController providerSignInController(final ConnectionFactoryRegistry connectionFactoryRegistry) {

        InMemoryUsersConnectionRepository inMemoryUsersConnectionRepository = new InMemoryUsersConnectionRepository(
                connectionFactoryRegistry);
        inMemoryUsersConnectionRepository.setConnectionSignUp(signUpAdapter);

        return new ProviderSignInController(connectionFactoryRegistry,
                                            inMemoryUsersConnectionRepository,
                                            new CustomSignInAdapter());
    }

    @Bean
    public ConnectionFactoryRegistry connectionFactoryRegistry() {
        ConnectionFactoryRegistry connectionFactoryRegistry = new ConnectionFactoryRegistry();
        FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory(facebookId, facebookSecret);
        GoogleConnectionFactory googleConnectionFactory = new GoogleConnectionFactory(googleId, googleSecret);
        GitHubConnectionFactory gitHubConnectionFactory = new GitHubConnectionFactory(githubId, githubSecret);
        facebookConnectionFactory.setScope("email");
        googleConnectionFactory.setScope("email");
        gitHubConnectionFactory.setScope("user:email");
        connectionFactoryRegistry.setConnectionFactories(Arrays.asList(facebookConnectionFactory,
                                                                       googleConnectionFactory,
                                                                       gitHubConnectionFactory));
        return connectionFactoryRegistry;
    }


}