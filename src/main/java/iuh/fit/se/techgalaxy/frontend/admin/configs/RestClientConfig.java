package iuh.fit.se.techgalaxy.frontend.admin.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RestClientConfig implements WebMvcConfigurer {
    @Bean
    RestClient restClient() {
        return RestClient.builder().build();
    }
}
