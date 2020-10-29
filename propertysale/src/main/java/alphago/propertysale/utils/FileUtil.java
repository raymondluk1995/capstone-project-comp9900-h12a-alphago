package alphago.propertysale.utils;

import alphago.propertysale.entity.User;
import org.apache.shiro.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: propertysale
 * @description: upload file to the server based on user id
 * @author: XIAO HAN
 * @create: 2020-10-08 00:04
 **/
public class FileUtil {
    private static String remote = "http://localhost:8060/img/";
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
        String path = user.getAvatarType() == null ?
                "default/" + Character.toUpperCase(user.getFirstname().charAt(0)) + ".png" :
                user.getUid() + "/avatar/avatar" + user.getAvatarType();
        return remote + path;
    }

    public static String getUserAvatar(long uid, String type){
        String path = uid + "/avatar/avatar" + type;
        return remote + path;
    }

    /**
    * @Description: Get file's type
    * @Author: Xiaohan
    * @Date: 9/10/20
    */
    public static String getType(String fileName){
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    /**
    * @Description: Get property cover from image server.
    * @Author: Xiaohan
    * @Date: 14/10/20
    */
    public static List<String> getImages(long pid){
        RestTemplate restTemplate = new RestTemplate();
        String[] names = restTemplate.getForObject(remote + "cover" + "/" + pid, String[].class);
        List<String> paths = new ArrayList<>();
        Assert.notNull(names);
        for(String name : names){
            paths.add(remote + "property/"  + pid + "/" + name);
        }
        return paths;
    }
}