package alphago.propertysale;

import alphago.propertysale.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

class PropertysaleApplicationTests {

	@Autowired
	private NotificationService notificationService;

	@Test
	void contextLoads() {

	}

}
