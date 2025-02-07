package microsservico.autenticacao.api.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import microsservico.autenticacao.api.Models.Usuario;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    @GetMapping
    public String getUsuarios() {
        System.out.println("Testando 1234");
        return String.format("testando %d", 1300);
    }
    

    // @GetMapping
    // public void getUsuarioId(@RequestParam String id) {
    //     System.out.println(id);
    // }
    
    
    @PostMapping
    public void cadastrar(@RequestBody Usuario novoUsuario) {
        System.out.println(novoUsuario);
    }
    
}
