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
import org.springframework.web.util.UriComponentsBuilder;
import microsservico.autenticacao.api.domain.models.LoginDTO;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
    
    @Autowired
    AuthenticationManager authManager;


    @PostMapping("/login") // rota /usuarios/login
    public ResponseEntity<Authentication> login(@RequestBody LoginDTO loginDto, UriComponentsBuilder uriBuilder) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.senha());
        System.out.println(token);
        Authentication usuarioLogado = authManager.authenticate(token);
        System.out.println(usuarioLogado);
        
        // URI uri = uriBuilder.path("/sessao/{id}").buildAndExpand("1").toUri();
        return ResponseEntity.ok().body(usuarioLogado);
    }
}
