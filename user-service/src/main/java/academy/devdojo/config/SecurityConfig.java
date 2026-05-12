package academy.devdojo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String[] WHITE_LIST = {"/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**", "/csrf"};
    // csrf: cross site request forgery
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) { // autenticação em memória (nunca usar na vida real, apenas em entrevistas, projetos rápidos)
        var user = User.withUsername("takamura")
                .password(encoder.encode("ippo"))
                .roles("USER")
                .build();

        var admin = User.withUsername("admin")
                .password(encoder.encode("devdojo"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin); // Quando for usar segurança, utilize esses usuários definidos aqui
    }

    @Bean // bean de autorização
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // filtro de segurança
        return http
                .csrf(AbstractHttpConfigurer::disable) // csrf -> csrf.disable()
//                .csrf(csrf -> csrf.csrfTokenRepository(new CookieCsrfTokenRepository()).csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(WHITE_LIST).permitAll().anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { // funcionamento da criptografia --> a senha criptografada é salva no banco de dados quando o usuário é criado.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // Quando você tenta logar, a senha que você usar é criptografada e comparada com a senha criptografada salva no banco de dados.
    }
}
