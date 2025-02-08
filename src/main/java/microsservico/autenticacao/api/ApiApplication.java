package microsservico.autenticacao.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
// import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
	
	// O projeto já tem o servidor de aplicação Tomcat como dependência - herdada das dependências do spring boot (tag parent no pom.xml)
	// https://code.visualstudio.com/docs/java/java-spring-boot
	
	
	// // Reparar migrations flyway
	// @Bean
	// public FlywayMigrationStrategy cleanMigrateStrategy() {
	// 	return flyway -> {
	// 		flyway.repair();
	// 		flyway.migrate();
	// 	};
	// }


	// Comando SQL para resetar migrations: delete from flyway_schema_history where success = 0;
	// O comando anterior serve para apagar na tabela do Flyway todas as migrations cuja execução falhou. 
	// Após isso, basta corrigir o código da migration e executar novamente o projeto.
}

// IoC - Inversion of Control (com injeção de dependência)
// https://www.reddit.com/r/SpringBoot/comments/y8xitr/comment/itcll0r/?utm_source=share&utm_medium=web3x&utm_name=web3xcss&utm_term=1&utm_content=share_button
// https://www.dio.me/articles/beans-entendendo-a-base-fundamental-do-spring-framework