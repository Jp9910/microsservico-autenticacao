package microsservico.autenticacao.api.domain.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUsuarioDTO (
    @NotBlank // string não pode ser vazia
    @Email
    String email,
    
    @NotBlank
    @Size(min = 6, message = "{validation.senha.size.too_short}")
    String senha,
    
    @NotBlank
    String nome
) {}
