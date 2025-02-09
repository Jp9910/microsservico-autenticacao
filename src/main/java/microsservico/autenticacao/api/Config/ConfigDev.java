package microsservico.autenticacao.api.Config;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class ConfigDev { // implement datasourceconfig class and override methods
    
}
