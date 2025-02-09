package microsservico.autenticacao.api.infra;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice // Indica que é uma classe tratadora de erros nos controllers da api
public class TratadorDeExceptions {
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> catch404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> catch400(MethodArgumentNotValidException excep) {
        List<FieldError> erros = excep.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new));
    }

    private record DadosErroValidacao (String campo, String mensagem) {
        public DadosErroValidacao (FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}
