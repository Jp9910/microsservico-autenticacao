package microsservico.autenticacao.api.infra;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import jakarta.annotation.PostConstruct;

@Configuration
public class DBConfig {
    
    @Value("${spring.datasource.username}") // Pegar o valor da variável do application.yaml
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String url;

    @Autowired
    private Environment env;
    
    @Bean
    public DataSource getDataSource() {
        System.out.println("GETDATASOURCE\n\n\n\n");
        System.out.println("$:");
		System.out.println(env.getProperty("SPRING_DATASOURCE_USERNAME"));
		System.out.println(env.getProperty("SPRING_DATASOURCE_PASSWORD"));
		System.out.println(env.getProperty("SPRING_DATASOURCE_URL"));
		System.out.println(env.getProperty("SPRING_ACTIVE_PROFILE"));
		System.out.println(env.getProperty("SPRING_ACTIVE_PROFILE"));
		System.out.println("\n\n\n\n");
        System.out.println("caminho:");
		System.out.println(env.getProperty("spring.datasource.username"));
		System.out.println(env.getProperty("spring.datasource.password"));
		System.out.println(env.getProperty("spring.datasource.url"));
		System.out.println(env.getProperty("spring.profiles.active"));
		System.out.println(env.getProperty("spring.datasource.driver-class-name"));
		System.out.println("\n\n\n\n");

        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(this.url);
        dataSourceBuilder.username(this.username);
        dataSourceBuilder.password(this.password);
        return dataSourceBuilder.build();
    }

    @PostConstruct
    public void init() {
        System.out.println("INIT\n\n\n\n");
        System.out.println("$:");
		System.out.println(env.getProperty("SPRING_DATASOURCE_USERNAME"));
		System.out.println(env.getProperty("SPRING_DATASOURCE_PASSWORD"));
		System.out.println(env.getProperty("SPRING_DATASOURCE_URL"));
		System.out.println(env.getProperty("SPRING_ACTIVE_PROFILE"));
		System.out.println(env.getProperty("SPRING_ACTIVE_PROFILE"));
		System.out.println("\n\n\n\n");
        System.out.println("caminho:");
		System.out.println(env.getProperty("spring.datasource.username"));
		System.out.println(env.getProperty("spring.datasource.password"));
		System.out.println(env.getProperty("spring.datasource.url"));
		System.out.println(env.getProperty("spring.profiles.active"));
		System.out.println(env.getProperty("spring.datasource.driver-class-name"));
		System.out.println("\n\n\n\n");
    }
}