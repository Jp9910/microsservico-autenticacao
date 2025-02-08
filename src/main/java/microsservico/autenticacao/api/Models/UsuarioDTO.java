package microsservico.autenticacao.api.Models;

public record UsuarioDTO (
    String email,
    String senha,
    String nome,
    Boolean isAdmin
) {}