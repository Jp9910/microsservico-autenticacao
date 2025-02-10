package microsservico.autenticacao.api.infra.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// A implementação de Filtros dos servlets do java (sem relação com o spring) seria feita assim:
    // import jakarta.servlet.Filter;
    // public class SecurityFilter implements Filter {...}


// Usar a implementação do Spring traz as vantagens de alto-nível do spring
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    // Ponto inicial do fluxo do sistema quando recebe uma requisição http
    // Método que o spring vai chamar quando o filtro for executado, que é quando chegar uma requisição para o servidor
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            throw new RuntimeException("Token de autorização não foi enviado no cabeçalho da requisição.");
        }
        String tokenJWT = authHeader.replace("Bearer ", "");
        System.out.println(tokenJWT);

        String email = tokenService.validarTokenEPegarEmail(tokenJWT);
        System.out.println(email);

        filterChain.doFilter(request, response); // Chamar o próximo filtro, ou seguir para o controller caso seja o último filtro.
    }
}
