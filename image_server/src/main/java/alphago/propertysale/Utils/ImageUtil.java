package alphago.propertysale.Utils;

import alphago.propertysale.entity.AvatarPorter;
import org.springframework.util.ClassUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @program: image_server
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-09 17:34
 **/
public class ImageUtil {
    private static final String PATH = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
    private static final String IMG = PATH + "/img/";

    public static void AvatarSave(AvatarPorter porter){
        byte[] bytes = porter.getAvatar();;
        long uid = porter.getUid();
        String type = porter.getType();

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
        path += "avatar" + type;
        File file = new File(path);
        try(FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream)) {
            outputStream.write(bytes);
        }catch (IOException e){
            System.out.println("fail");
        }
    }

    public static boolean isDirectoryExist(String path){
        File file = new File(path);
        return file.exists();
    }

}