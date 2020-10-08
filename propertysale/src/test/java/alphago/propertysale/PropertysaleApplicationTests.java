package alphago.propertysale;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ClassUtils;

import java.io.File;

//@SpringBootTest
class PropertysaleApplicationTests {
	private static final String PATH = ClassUtils.getDefaultClassLoader().getResource("static").getPath() + "/";
	private static final String IMG = PATH + "/img/";
	@Test
	void contextLoads() {
		String path = IMG + "uid/" + "avatar/";
		if(!isDirectoryExist(path)) new File(path).mkdir();
	}

	public static boolean isDirectoryExist(String path){
		File file = new File(path);
		return file.exists();
	}

}
