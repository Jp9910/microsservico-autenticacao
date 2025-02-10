package microsservico.autenticacao.api.infra.security;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import microsservico.autenticacao.api.domain.models.Usuario;

@Service
public class TokenService {

    @Value("${api.security.senha-assinatura-token}") // Pegar o valor da variável do application.yaml
    private String senhaAssinatura;

    public String gerarTokenJWT(Usuario usuario) { 
        try {
            var algoritmo = Algorithm.HMAC256(senhaAssinatura);
            return JWT.create()
                .withIssuer("API Microsservico.Autenticacao")
                .withSubject(usuario.getEmail())
                .withClaim("id", usuario.getId()) // (String que identifica o nome da propriedade, informação que se deseja armazenar)
                .withExpiresAt(dataExpiracao())
                .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("erro ao gerrar token jwt", exception);
        }		
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
