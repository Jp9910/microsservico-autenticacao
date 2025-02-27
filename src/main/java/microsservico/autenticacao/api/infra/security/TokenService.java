package microsservico.autenticacao.api.infra.security;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import microsservico.autenticacao.api.domain.models.Usuario;

@Service
public class TokenService {

    @Value("${api.security.senha-assinatura-token}") // Pegar o valor da variável do application.yaml
    private String senhaAssinatura;

    private static final String ISSUER = "API Microsservico.Autenticacao";

    // Chamado no AutenticacaoController
    public String gerarTokenJWT(Usuario usuario) { 
        try {
            var algoritmo = Algorithm.HMAC256(senhaAssinatura);
            return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(usuario.getEmail())
                .withClaim("id", usuario.getId()) // (String que identifica o nome da propriedade, informação que se deseja armazenar)
                .withClaim("isAdmin",usuario.getIsAdmin())
                .withClaim("nome",usuario.getNome())
                .withExpiresAt(dataExpiracao())
                .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("erro ao gerrar token jwt", exception);
        }		
    }

    // Chamado no filtro SecurityFilter para verificar se a requisição que está chegando está com token válido e pegar o email de quem gerou
    public String validarTokenEPegarEmail(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(senhaAssinatura);
            return JWT.require(algoritmo)
                            .withIssuer(ISSUER) // Verificar os dados da variável "issuer" do token
                            .build() // pegar o verificador de token (coisa do spring)
                            .verify(tokenJWT) // verificar se está válido
                            .getSubject(); // pegar o email de quem gerou o token
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
