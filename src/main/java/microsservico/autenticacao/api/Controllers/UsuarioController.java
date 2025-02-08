package microsservico.autenticacao.api.Controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import microsservico.autenticacao.api.Models.Usuario;
import microsservico.autenticacao.api.Models.CreateUsuarioDTO;
import microsservico.autenticacao.api.Models.ReadUsuarioDTO;
import microsservico.autenticacao.api.Models.UpdateUsuarioDTO;
import microsservico.autenticacao.api.Models.UsuarioRepository;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("usuarios")
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
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
    public Page<ReadUsuarioDTO> getUsuarios(@PageableDefault(size = 10, sort = {"isAdmin"}, direction = Direction.DESC) Pageable paginacao) {
        return repo.findAll(paginacao).map(ReadUsuarioDTO::new);
    }

    @GetMapping("ativos")
    public Page<ReadUsuarioDTO> getUsuariosAtivos(@PageableDefault(size = 10, sort = {"isAdmin"}, direction = Direction.DESC) Pageable paginacao) {
        // Da pra criar uma assinatura de método seguindo um padrão de nomeclatura para que o spring data
        // identifique que se trata de uma query e o sql já vem implementado
        return repo.findAllByIsAtivoTrue(paginacao).map(ReadUsuarioDTO::new);
    }
    

    @GetMapping("{id}")
    public ReadUsuarioDTO getUsuarioId(@PathVariable Long id) {
        Usuario user = repo.getReferenceById(id);
        return new ReadUsuarioDTO(user);
    }

    @PutMapping("{id}")
    @Transactional
    public void putMethodName(@PathVariable Long id, @RequestBody UpdateUsuarioDTO updateUsuarioDto) {
        System.out.println("teste put");
        Usuario user = repo.getReferenceById(id);
        if (updateUsuarioDto.nome() != null && !user.getNome().equals(updateUsuarioDto.nome())) {
            user.setNome(updateUsuarioDto.nome());
        }
        if (updateUsuarioDto.senha() != null) {
            String newSalt = this.generateRandomString();
            String digest = this.encryptPassword(updateUsuarioDto.senha(), newSalt);
            user.setSalt(newSalt);
            user.setSenha(digest);
        }
        System.out.println(user.toString());
    }
    
    
    @PostMapping("/cadastrar")
    @Transactional // Especificar que isso é uma transação (operação atômica)
    public void cadastrar(@RequestBody @Valid CreateUsuarioDTO createUsuarioDto) {
        //@valid indica que o campo precisa ser validado de acordo com as anotações na classe que está sendo validada
        System.out.println(createUsuarioDto);
        String salt = this.generateRandomString();
        String digest = this.encryptPassword(createUsuarioDto.senha(), salt);
        Usuario user = new Usuario(createUsuarioDto, digest, salt, false);
        System.out.println(user.toString());
        repo.save(user);
    }

    @PostMapping("/cadastraradmin")
    @Transactional
    public void cadastrarAdmin(@RequestBody @Valid CreateUsuarioDTO createAdminDto) {
        // TODO: Proteger o endpoint para evitar que qualquer um consiga cadastrar um admin
        System.out.println("Criar usuario admin");
        String salt = this.generateRandomString();
        String digest = this.encryptPassword(createAdminDto.senha(), salt);
        Usuario user = new Usuario(createAdminDto, digest, salt, true);
        System.out.println(user.toString());
        repo.save(user);
    }
    

    @PostMapping("/login") // rota /usuarios/login
    public String login(@RequestParam String usuario, @RequestParam String senha) {
        return String.format("testando login");
    }

    @DeleteMapping("{id}")
    @Transactional
    public void removerUsuario(@PathVariable Long id) {
        // repo.deleteById(id); // Isso seria um hard delete
        Usuario user = repo.getReferenceById(id);
        user.setIsAtivo(false);
    }

    private String encryptPassword(String senha, String salt) {
        String digest = this.encoder.encode(senha.concat(salt));
        System.out.println("hash: ".concat(digest));
        return digest;
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

        System.out.println("Salt gerado: ".concat(generatedString));
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