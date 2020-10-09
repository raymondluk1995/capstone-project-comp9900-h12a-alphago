package alphago.propertysale.utils;

import alphago.propertysale.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
    * @Description: Upload User's Avatar
    * @Author: Xiaohan
    * @Date: 8/10/20
    */
    @Deprecated
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
        //save avatar
        String name = file.getOriginalFilename();
        path += "avatar" + name.substring(name.lastIndexOf("."));
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

    public static String getUserAvatar(User user){
        return user.getAvatarType() == null ?
                "default/" + Character.toUpperCase(user.getFirstname().charAt(0)) + ".png" :
                user.getUid() + "/avatar/avatar" + user.getAvatarType();
    }

    /**
    * @Description: Get file's type
    * @Author: Xiaohan
    * @Date: 9/10/20
    */
    public static String getType(String fileName){
        return fileName.substring(fileName.lastIndexOf('.'));
    }
}