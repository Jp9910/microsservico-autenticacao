package microsservico.autenticacao.api.Models;

public record Usuario (
    String email,
    String senha,
    String nome,
    Boolean isAdmin
) {}