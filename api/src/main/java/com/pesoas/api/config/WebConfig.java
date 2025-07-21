package com.pesoas.api.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica a todas as rotas da API (ex: /pessoas-fisicas/**)
                .allowedOrigins("http://localhost:4200") // Origem do seu frontend Angular
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // Métodos HTTP permitidos
                .allowedHeaders("*") // Quais cabeçalhos são permitidos na requisição
                .allowCredentials(true) // Se você precisar enviar cookies ou autenticação (opcional)
                .maxAge(3600); // Quanto tempo (em segundos) o resultado de uma requisição pre-flight OPTIONS pode ser cacheado
    }
}