package microsservico.autenticacao.api.Models;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="usuarios")
@Entity(name="usuario")
// As anotações a seguir são do lombok para gerar código boilerplate
@Getter // Gera os getters da classe
@NoArgsConstructor // Gera o construtor sem argumentos
@AllArgsConstructor // Gera o construtor com todos os argumentos
@EqualsAndHashCode(of = "id") // Gera um comparador usando o campo id
public class Usuario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String senha;
    private String salt;
    private String nome;
    private Boolean is_admin;

    public Usuario(UsuarioDTO dto, String digest, String salt) {
        this.email = dto.email(); 
        this.nome = dto.nome(); 
        this.is_admin = dto.isAdmin(); 
        this.senha = digest;
        this.salt = salt;
    }
}
