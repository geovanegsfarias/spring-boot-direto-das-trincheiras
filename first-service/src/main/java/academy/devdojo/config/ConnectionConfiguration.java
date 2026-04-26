package academy.devdojo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ConnectionConfiguration {
    @Value("${database.url}")
    private String url;
    @Value("${database.username}")
    private String username;
    @Value("${database.password}")
    private String password;

    @Bean
    @Profile("mysql") // esse bean só é criado se o profile ativo for "mysql"
    public Connection connectionMySql() {
        return new Connection(url, username, password);
    }

    @Bean
    @Profile("mongo") // esse bean só é criado se o profile ativo for "mongo"
    public Connection connectionMongo() {
        return new Connection(url, username, password);
    }

    @Bean
    @Profile("test") // esse bean só é criado se o profile ativo for "test"
    public Connection connectionTest() {
        return new Connection(url, username, password);
    }
}