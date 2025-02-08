package microsservico.autenticacao.api.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import microsservico.autenticacao.api.Models.Usuario;
import microsservico.autenticacao.api.Models.UsuarioDTO;
import microsservico.autenticacao.api.Models.UsuarioRepository;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    // @Autowired // Com isso o spring passará esse atributo assim que instanciar o controller (injeção de dependência feita pelo framework)
    @Autowired
    private UsuarioRepository repo;
    private BCryptPasswordEncoder encoder;
    
    public UsuarioController () {
        // https://argon2.online/
        // RFC Argon2: https://datatracker.ietf.org/doc/rfc9106/
        this.encoder = new BCryptPasswordEncoder();
    }

    @GetMapping
    public String getUsuarios() {
        System.out.println("Testando 1234");
        System.out.println(this.encoder.encode("asdf"));
        String salt = this.generateRandomString();
        System.out.println("salt: ".concat(salt));
        return String.format("testando %d", 1300);
    }
    

    // @GetMapping
    // public void getUsuarioId(@RequestParam String id) {
    //     System.out.println(id);
    // }
    
    
    @PostMapping
    @Transactional // Especificar que isso é uma transação (operação atômica)
    public void cadastrar(@RequestBody UsuarioDTO novoUsuario) {
        System.out.println(novoUsuario);
        String salt = this.generateRandomString();
        System.out.println("salt: ".concat(salt));
        String digest = encoder.encode(novoUsuario.senha().concat(salt));
        System.out.println("hash: ".concat(digest));
        Usuario user = new Usuario(novoUsuario, digest, salt);
        System.out.println("User: ");
        System.out.println(user.toString());
        repo.save(user);
    }

    @PostMapping("/login") // rota /usuarios/login
    public String login(@RequestParam String usuario, @RequestParam String senha) {
        return String.format("testando login");
    }

    private String generateRandomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 16;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();

        System.out.println(generatedString);
        return generatedString;
    }
    
}

// Sobre DAOs e Repositories:
// DAOs - lidam diretamente com a fonte de dados e abstraem as operações realizadas nela. 
// Repositórios - provêm uma interface para tratar o armazenamento e recuperação de entidades do domínio como uma coleção.
// explicação completa: https://cursos.alura.com.br/course/spring-boot-3-desenvolva-api-rest-java/task/116059
// fonte: https://pt.stackoverflow.com/a/114683
// DAOs geralmente tem métodos mais genéricos: get(), update(), create(), delete() 
// enquanto repos normalmente tem métodos mais representantes de entidades do domínio: atualizarSalario(), marcarPedido(), getUsuariosInativos()