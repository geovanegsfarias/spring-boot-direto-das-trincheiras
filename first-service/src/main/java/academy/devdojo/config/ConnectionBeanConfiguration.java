package academy.devdojo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
public class ConnectionBeanConfiguration {
    private final ConnectionConfigurationProperties configurationProperties;

    @Bean
    @Profile("mysql") // esse bean só é criado se o profile ativo for "mysql"
    public Connection connectionMySql() {
        return new Connection(
                configurationProperties.url(),
                configurationProperties.username(),
                configurationProperties.password());
    }

    @Bean
    @Profile("mongo") // esse bean só é criado se o profile ativo for "mongo"
    public Connection connectionMongo() {
        return new Connection(
                configurationProperties.url(),
                configurationProperties.username(),
                configurationProperties.password());
    }

    @Bean
    @Profile("test") // esse bean só é criado se o profile ativo for "test"
    public Connection connectionTest() {
        return new Connection(
                configurationProperties.url(),
                configurationProperties.username(),
                configurationProperties.password());
    }
}