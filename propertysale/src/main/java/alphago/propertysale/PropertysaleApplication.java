package alphago.propertysale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PropertysaleApplication {

	public static void main(String[] args) {
		SpringApplication.run(PropertysaleApplication.class, args);
	}

}
