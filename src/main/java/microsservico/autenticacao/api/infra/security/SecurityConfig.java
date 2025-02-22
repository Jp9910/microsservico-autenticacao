package microsservico.autenticacao.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    SecurityFilter filtroDeSegurancaCustomizado;


    // O @Bean serve para exportar uma classe para o spring, fazendo com que ele
    // consiga carregá-la e realize
    // a sua injeção de dependência em outras classes.

    // Configurar como a autenticação será feita na aplicação - de modo stateless e
    // sem proteção contra csrf
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers("/auth/login").permitAll(); // permitir qualquer requisição para login
                    req.requestMatchers("/usuarios/cadastrar").permitAll(); // permitir qualquer requisição para cadastrar não-admin
                    // De resto, apenas admins podem visualizar e editar usuários
                    req.requestMatchers(HttpMethod.GET, "/usuarios").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.GET, "/usuarios/ativos").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.GET, "/usuarios/{id}").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.PUT, "/usuarios/{id}").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.POST, "/usuarios/cadastraradmin").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/usuarios/{id}").hasRole("ADMIN");
                    req.anyRequest().authenticated(); // qualquer outra rota, permitir apenas usuários autenticados
                })
                // o filtro padrão do spring security para autenticação é rodado antes de tudo por padrão,
                // então é preciso explicitar que o filtro criado deve ser rodado antes para evitar
                .addFilterBefore(filtroDeSegurancaCustomizado, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Configurar o Autowired da classe que gerenciará a autenticação no
    // AutenticacaoController
    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Configurar qual será o encoder dos passwords
    // BCrypt already has built-in salt:
    // https://stackoverflow.com/questions/6832445/how-can-bcrypt-have-built-in-salts
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
