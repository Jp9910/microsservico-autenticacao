package microsservico.autenticacao.api.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsFilterConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // config.setAllowCredentials(true); // não é necessário. mas tmb n sei oq faz exatamente
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedOrigin("http://localhost:80");
        config.addAllowedOrigin("http://gateway:80");
        config.addAllowedOrigin("http://192.168.49.1:5173"); // ip da máquina virtual (para testes, apagar depois)
        config.addAllowedOrigin("http://192.168.49.1:4200"); // ip da máquina virtual (para testes, apagar depois)
        config.addAllowedOrigin("http://192.168.49.1:80");
        config.addAllowedOrigin("*"); // >> REMOVER AO LANÇAR PUBLICAMENTE
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
