package academy.devdojo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "database")
public record ConnectionConfigurationProperties(String url, String username, String password) {
}
// lê as propriedades do application.yml que começam com database e mapeia automaticamente nos campos da classe, sem precisar usar @Value em cada um.