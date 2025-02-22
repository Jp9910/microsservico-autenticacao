package microsservico.autenticacao.api.infra.security;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import microsservico.autenticacao.api.domain.UsuarioRepository;

// A implementação de Filtros dos servlets do java (sem relação com o spring) seria feita assim:
    // import jakarta.servlet.Filter;
    // public class SecurityFilter implements Filter {...}


// Usar a implementação do Spring traz as vantagens de alto-nível do spring
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepo;

    // >>> PONTO INICIAL <<< do fluxo do sistema quando recebe uma requisição http
    // Método que o spring vai chamar quando o filtro for executado, que é quando chegar uma requisição para o servidor
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        // requisições para a rota de login não precisam do header        
        if (authHeader != null) {
            String tokenJWT = authHeader.replace("Bearer ", "").trim();
            String email = tokenService.validarTokenEPegarEmail(tokenJWT);

            System.out.println(tokenJWT);
            System.out.println(email);

            UserDetails usuario = usuarioRepo.findByEmail(email);
            if (usuario == null) {throw new UsernameNotFoundException("Usuário foi excluido depois de gerar o token.");}
            System.out.println(usuario);
            Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

            // Chegando aqui, significa O token está válido, ou seja, é de alguém autenticado no sistema
            // Agora pode-se usar o email da pessoa para checar se ela tem autorização para 
            // seguir com a requisição, o que é feito pelas configurações de permissões no 
            // método securityFilterChain do arquivo SecurityConfig.java

            SecurityContextHolder.getContext().setAuthentication(authentication); // define um usuário como logado
        }

        // Chegando aqui, significa que passou pela verificação acima OU o usuário não mandou o token no cabeçalho
        // então será passado para o spring determinar, por meio das configurações no SecurityConfig.java se o usuário
        // pode ou não continuar com a requisição (a única possível seria a de login)

        filterChain.doFilter(request, response); // Chamar o próximo filtro, ou seguir para o controller caso seja o último filtro.
        // Próximo ponto: função do controller (se passar pelas configurações de segurança das rotas e roles no SecurityConfig.java)
    }
}
