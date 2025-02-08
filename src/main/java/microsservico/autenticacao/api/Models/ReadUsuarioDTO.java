package microsservico.autenticacao.api.Models;

public record ReadUsuarioDTO (
    Long id,
    String email,
    String nome,
    Boolean isAdmin,
    Boolean isAtivo
) {
    public ReadUsuarioDTO (Usuario usuario) {
        this(usuario.getId(),usuario.getEmail(), usuario.getNome(), usuario.getIsAdmin(), usuario.getIsAtivo());
    }
}
