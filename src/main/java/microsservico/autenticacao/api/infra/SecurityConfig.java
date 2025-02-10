package microsservico.autenticacao.api.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    // O @Bean serve para exportar uma classe para o spring, fazendo com que ele consiga carregá-la e realize
    // a sua injeção de dependência em outras classes. 
    
    // Configurar como a autenticação será feita na aplicação - de modo stateless e sem proteção contra csrf
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).build();
    }

    // Configurar o Autowired da classe que gerenciará a autenticação no AutenticacaoController
    @Bean
    public AuthenticationManager authManager (AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Configurar qual será o encoder dos passwords
    // BCrypt already has built-in salt: https://stackoverflow.com/questions/6832445/how-can-bcrypt-have-built-in-salts
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
