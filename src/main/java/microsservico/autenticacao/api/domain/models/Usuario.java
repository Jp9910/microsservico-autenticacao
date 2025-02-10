package microsservico.autenticacao.api.domain.models;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Boolean isAdmin = false; // O spring já converte o camelCase para snake_case quando busca informações no banco
    @Setter private String nome;
    @Setter private String senha;
    @Setter private String salt;
    @Setter private Boolean isAtivo;

    public Usuario(CreateUsuarioDTO dto, String digest, String salt, Boolean isAdmin) {
        this.email = dto.email(); 
        this.nome = dto.nome();
        this.isAdmin = isAdmin;
        this.senha = digest;
        this.salt = salt;
        this.isAtivo = true;
    }

    public String toString() { 
        return "User:\n\temail: ".concat(email)
                .concat("\n\tnome: ").concat(this.nome)
                .concat("\n\tsenha: ").concat(this.senha)
                .concat("\n\tsalt: ").concat(this.salt)
                .concat("\n\tisAdmin: ").concat(this.isAdmin.toString());
    } 
}
