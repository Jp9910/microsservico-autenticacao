package microsservico.autenticacao.api.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import microsservico.autenticacao.api.domain.models.LoginDTO;
import microsservico.autenticacao.api.domain.models.Usuario;
import microsservico.autenticacao.api.infra.security.TokenService;

@Slf4j // usar o lombok para não precisar declarar o logger explicitamente como no ApiApplication.java
@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
    
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    // private static Logger logger = LoggerFactory.getLogger(ApiApplication.class);

    @PostMapping("/login") // rota /auth/login
    public ResponseEntity<TokenDto> login(@RequestBody LoginDTO loginDto) {
        log.info("Login requisitado");
        log.info("ã");
        log.info("✅");
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.senha());
        try {
            Authentication autenticacao = authManager.authenticate(authToken); // vai usar o BCrypt, pois foi configurado SecurityConfig.java
            String tokenJWT = tokenService.gerarTokenJWT((Usuario) autenticacao.getPrincipal());
            authSuccesses.increment(); // Incrementar métrica personalizada de autenticações com sucesso
            log.info("Login com sucesso");
            return ResponseEntity.ok(new TokenDto(tokenJWT));
        } catch (AuthenticationException e) {
            log.info("Login com falha");
            authFails.increment(); // Incrementar métrica personalizada de autenticações com falha
            throw e; // o TratadorDeExceptions vai lidar com a exception. (Talvez seja melhor colocar a métrica de falha lá)
        }
    }

    private record TokenDto (String token) {}

    // Métricas personalizadas de autenticações com sucesso ou falha
    // Tipos de métricas no prometheus: https://prometheus.io/docs/concepts/metric_types/#metric-types
    Counter authSuccesses; // O counter é zerado quando a aplicação é reinicializada, mas o histórico dela permanece no prometheus_data
    Counter authFails;
    public AutenticacaoController(MeterRegistry registry_de_metricas) { // injetar o registry no controller
        authSuccesses = Counter.builder("auth_user_sucess")
            .description("Autenticações com sucesso")
            .register(registry_de_metricas);
            
        authFails = Counter.builder("auth_user_fails")
            .description("Autenticações com falha")
            .register(registry_de_metricas);
    }
}


// Explicação do JWT:
// https://cursos.alura.com.br/extra/alura-mais/o-que-e-json-web-token-jwt--c203
// Basicamente é um objeto encryptado que contém informações, tal como usuário, senha e permissões,
// mas caso seja alterado (para alterar as permissões por exemplo) ele se torna inválido