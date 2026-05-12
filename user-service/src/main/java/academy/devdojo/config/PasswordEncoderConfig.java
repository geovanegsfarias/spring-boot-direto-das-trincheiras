package academy.devdojo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() { // funcionamento da criptografia --> a senha criptografada é salva no banco de dados quando o usuário é criado.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // Quando você tenta logar, a senha que você usar é criptografada e comparada com a senha criptografada salva no banco de dados.
    }
}
