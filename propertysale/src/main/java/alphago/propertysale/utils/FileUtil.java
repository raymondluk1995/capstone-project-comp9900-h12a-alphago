package alphago.propertysale.utils;

import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @program: propertysale
 * @description: upload file to the server based on user id
 * @author: XIAO HAN
 * @create: 2020-10-08 00:04
 **/
public class FileUtil {
    private static final String PATH = ClassUtils.getDefaultClassLoader().getResource("static").getPath() + "/";
    private static final String IMG = PATH + "/img/";

    public static void uploadAvatar(MultipartFile file , String uid) throws IOException {
        String path = IMG  +uid + "/";
        if(!isDirectoryExist(path)) new File(path).mkdir();
        path += "avatar/";
        if(!isDirectoryExist(path)) new File(path).mkdir();
        File parent = new File(path);
        // delete exist old file
        File[] files = parent.listFiles();
        assert files != null;
        Arrays.stream(files).forEach(File::delete);
        String name = file.getOriginalFilename();
        path += "avatar" + name.substring(name.lastIndexOf("."));
        System.out.println(path);
        file.transferTo(Paths.get(path));
    }

    /**
    * @Description: Check if the directory exist
    * @Author: Xiaohan
    * @Date: 8/10/20
    */
    public static boolean isDirectoryExist(String path){
        File file = new File(path);
        return file.exists();
    }
}