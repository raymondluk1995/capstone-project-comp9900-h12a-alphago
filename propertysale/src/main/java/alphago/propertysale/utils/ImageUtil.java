package alphago.propertysale.utils;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @program: propertysale
 * @description: Image
 * @author: XIAO HAN
 * @create: 2020-10-07 16:09
 **/
public class ImageUtil {
    private static String path = "/home/zxhhaha/myPic/avatars/";
    public static void saveAvatar(MultipartFile avatar , Long uid) throws IOException {
        File file = new File(path + uid + ".png");
        if(file.exists()) file.delete();
        String name = avatar.getOriginalFilename();
        name = uid + name.substring(name.lastIndexOf("."));
//        ImgUtil.convert(avatar , FileUtil.file(path + name));
    }
}