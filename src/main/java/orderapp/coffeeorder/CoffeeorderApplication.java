package orderapp.coffeeorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CoffeeorderApplication {
	public static void main(String[] args) {
		SpringApplication.run(CoffeeorderApplication.class, args);
	}

}
