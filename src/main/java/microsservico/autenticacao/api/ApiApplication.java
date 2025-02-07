package microsservico.autenticacao.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
	
	// O projeto já tem o servidor de aplicação Tomcat como dependência - herdada das dependências do spring boot (tag parent no pom.xml)
	// https://code.visualstudio.com/docs/java/java-spring-boot
}
