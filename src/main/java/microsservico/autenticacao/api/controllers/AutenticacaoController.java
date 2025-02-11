package microsservico.autenticacao.api.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import microsservico.autenticacao.api.domain.models.LoginDTO;
import microsservico.autenticacao.api.domain.models.Usuario;
import microsservico.autenticacao.api.infra.security.TokenService;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
    
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;


    @PostMapping("/login") // rota /auth/login
    public ResponseEntity<TokenDto> login(@RequestBody LoginDTO loginDto) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.senha());

        Authentication autenticacao = authManager.authenticate(authToken); // vai usar o BCrypt, pois foi configurado SecurityConfig.java

        String tokenJWT = tokenService.gerarTokenJWT((Usuario) autenticacao.getPrincipal());

        return ResponseEntity.ok(new TokenDto(tokenJWT));
    }

    private record TokenDto (String token) {}
}


// Explicação do JWT:
// https://cursos.alura.com.br/extra/alura-mais/o-que-e-json-web-token-jwt--c203
// Basicamente é um objeto encryptado que contém informações, tal como usuário, senha e permissões,
// mas caso seja alterado (para alterar as permissões por exemplo) ele se torna inválido