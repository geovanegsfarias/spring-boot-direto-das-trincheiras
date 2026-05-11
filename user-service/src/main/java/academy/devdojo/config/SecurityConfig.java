package academy.devdojo.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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

    @Bean
    public PasswordEncoder passwordEncoder() { // funcionamento da criptografia --> a senha criptografada é salva no banco de dados quando o usuário é criado.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // Quando você tenta logar, a senha que você usar é criptografada e comparada com a senha criptografada salva no banco de dados.
    }
}
