package alphago.propertysale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *  Run this class to start the whole system
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class PropertysaleApplication {

	public static void main(String[] args) {
		SpringApplication.run(PropertysaleApplication.class, args);
	}

}
