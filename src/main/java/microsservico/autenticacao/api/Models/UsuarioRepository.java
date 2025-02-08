package microsservico.autenticacao.api.Models;

import org.springframework.data.jpa.repository.JpaRepository;

// O JpaRepository faz todo o mapeamento dos dados do banco para o Model (usando o padrão Repository)
// JpaRepository<Tipo da entidade que esse repositório vai conter, tipo do atributo da chave primária>
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
}
