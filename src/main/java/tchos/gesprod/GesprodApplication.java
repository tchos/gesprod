package tchos.gesprod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("auth")
public class GesprodApplication {

	public static void main(String[] args) {
		SpringApplication.run(GesprodApplication.class, args);
	}

}
