package com.et.SudburyCityPlatform.configs;



import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI jobOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Job Application API")
                        .description("APIs for managing job openings")
                        .version("1.0.0"));
    }
}

