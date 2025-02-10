package microsservico.autenticacao.api.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import microsservico.autenticacao.api.domain.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // O JpaRepository faz todo o mapeamento dos dados do banco para o Model (usando o padrão Repository)
    // JpaRepository<Tipo da entidade que esse repositório vai conter, tipo do atributo da chave primária>

    // Da pra criar uma assinatura de método seguindo um padrão de nomeclatura para que o spring data
    // identifique que se trata de uma query e o sql já vem implementado
    // https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html

    Page<Usuario> findAllByIsAtivoTrue(Pageable paginacao);

    UserDetails findByEmail(String email);

}
