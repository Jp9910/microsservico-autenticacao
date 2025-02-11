package microsservico.autenticacao.api.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import microsservico.autenticacao.api.domain.UsuarioRepository;
import microsservico.autenticacao.api.domain.models.CreateUsuarioDTO;
import microsservico.autenticacao.api.domain.models.ReadUsuarioDTO;
import microsservico.autenticacao.api.domain.models.UpdateUsuarioDTO;
import microsservico.autenticacao.api.domain.models.Usuario;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/usuarios")
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
public class UsuarioController {

    // @Autowired // Com isso o spring passará esse atributo assim que instanciar o controller (injeção de dependência feita pelo framework)
    @Autowired
    private UsuarioRepository repo;
    private BCryptPasswordEncoder encoder;
    @Autowired
    Environment env;
    
    public UsuarioController () {
        // https://argon2.online/
        // RFC Argon2: https://datatracker.ietf.org/doc/rfc9106/
        this.encoder = new BCryptPasswordEncoder();
    }

    @GetMapping
    public ResponseEntity<Page<ReadUsuarioDTO>> getUsuarios(@PageableDefault(size = 10, sort = {"isAdmin"}, direction = Direction.DESC) Pageable paginacao) {
        Page<ReadUsuarioDTO> pagina = repo.findAll(paginacao).map(ReadUsuarioDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/ativos")
    public ResponseEntity<Page<ReadUsuarioDTO>> getUsuariosAtivos(@PageableDefault(size = 10, sort = {"isAdmin"}, direction = Direction.DESC) Pageable paginacao) {
        // Da pra criar uma assinatura de método seguindo um padrão de nomeclatura para que o spring data
        // identifique que se trata de uma query e o sql já vem implementado
        Page<ReadUsuarioDTO> pagina = repo.findAllByIsAtivoTrue(paginacao).map(ReadUsuarioDTO::new);
        return ResponseEntity.ok(pagina);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<ReadUsuarioDTO> getUsuarioId(@PathVariable Long id) {
        Usuario user = repo.getReferenceById(id);
        ReadUsuarioDTO userDto = new ReadUsuarioDTO(user);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ReadUsuarioDTO> atualizarUsuario(@PathVariable Long id, @RequestBody UpdateUsuarioDTO updateUsuarioDto) {
        Usuario user = repo.getReferenceById(id);
        if (updateUsuarioDto.nome() != null && !user.getNome().equals(updateUsuarioDto.nome())) {
            user.setNome(updateUsuarioDto.nome());
        }
        if (updateUsuarioDto.senha() != null) {
            String digest = this.encryptPassword(updateUsuarioDto.senha());
            user.setSenha(digest);
        }
        return ResponseEntity.ok(new ReadUsuarioDTO(user));
    }
    
    
    @PostMapping("/cadastrar")
    @Transactional // Especificar que isso é uma transação (operação atômica)
    //@valid indica que o campo precisa ser validado de acordo com as anotações na classe que está sendo validada
    public ResponseEntity<Object> cadastrar(@RequestBody @Valid CreateUsuarioDTO createUsuarioDto, UriComponentsBuilder uriBuilder) {
        var jaExiste = repo.findByEmail(createUsuarioDto.email());
        if (jaExiste != null) {
            return ResponseEntity.badRequest().body("Este email já está associado a um usuário.");
        }
        String digest = this.encryptPassword(createUsuarioDto.senha());
        Usuario user = new Usuario(createUsuarioDto, digest, false);
        repo.save(user);

        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(user.getId()).toUri();
        ReadUsuarioDTO dto = new ReadUsuarioDTO(user);
        return ResponseEntity.created(uri).body(dto);
    }

    // https://spring.io/guides/gs/securing-web
    // https://www.javaguides.net/2024/01/spring-boot-security-jwt-tutorial.html
    // https://www.javaguides.net/2018/10/user-registration-module-using-springboot-springmvc-springsecurity-hibernate5-thymeleaf-mysql.html
    @PostMapping("/cadastraradmin")
    @Transactional
    public ResponseEntity<Object> cadastrarAdmin(@RequestBody @Valid CreateUsuarioDTO createUsuarioDto, UriComponentsBuilder uriBuilder) {
        var jaExiste = repo.findByEmail(createUsuarioDto.email());
        if (jaExiste != null) {
            return ResponseEntity.badRequest().body("Este email já está associado a um usuário.");
        }
        String digest = this.encryptPassword(createUsuarioDto.senha());
        Usuario user = new Usuario(createUsuarioDto, digest, true);
        repo.save(user);

        URI uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(user.getId()).toUri();
        ReadUsuarioDTO dto = new ReadUsuarioDTO(user);
        return ResponseEntity.created(uri).body(dto);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> removerUsuario(@PathVariable Long id) {
        // repo.deleteById(id); // Isso seria um hard delete
        Usuario user = repo.getReferenceById(id);
        user.setIsAtivo(false);
        return ResponseEntity.noContent().build();
    }

    private String encryptPassword(String senha) {
        String digest = this.encoder.encode(senha);
        return digest;
    }

    // private String generateRandomString() {
    //     int leftLimit = 48; // numeral '0'
    //     int rightLimit = 122; // letter 'z'
    //     int targetStringLength = 16;
    //     Random random = new Random();

    //     String generatedString = random.ints(leftLimit, rightLimit + 1)
    //         .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
    //         .limit(targetStringLength)
    //         .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
    //         .toString();

    //     System.out.println("Salt gerado: ".concat(generatedString));
    //     return generatedString;
    // }
    
    // private void verProfileAtivo() {
    //     String[] profiles = env.getActiveProfiles();
    //     for (String st : profiles) {
    //         System.out.println(st);
    //     }
    // }
}

// Sobre DAOs e Repositories:
// DAOs - lidam diretamente com a fonte de dados e abstraem as operações realizadas nela. 
// Repositórios - provêm uma interface para tratar o armazenamento e recuperação de entidades do domínio como uma coleção.
// explicação completa: https://cursos.alura.com.br/course/spring-boot-3-desenvolva-api-rest-java/task/116059
// fonte: https://pt.stackoverflow.com/a/114683
// DAOs geralmente tem métodos mais genéricos: get(), update(), create(), delete() 
// enquanto repos normalmente tem métodos mais representantes de entidades do domínio: atualizarSalario(), marcarPedido(), getUsuariosInativos()