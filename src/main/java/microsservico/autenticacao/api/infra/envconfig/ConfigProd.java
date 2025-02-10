package microsservico.autenticacao.api.infra.envconfig;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class ConfigProd {
    
}
