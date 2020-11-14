package alphago.propertysale.utils;

import alphago.propertysale.entity.POJO.User;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: propertysale
 * @description: upload file to the server based on user id
 **/
public class FileUtil {
    private static String remote = "http://3.24.78.45:8060/img/";
    /**
    * @Description: Check if the directory exist
    */
    public static boolean isDirectoryExist(String path){
        File file = new File(path);
        return file.exists();
    }

    /**
    * @Description: Get User's avatar from Image Server
    */
    public static String getUserAvatar(User user){
        String path = user.getAvatarType() == null ?
                "default/" + Character.toUpperCase(user.getFirstname().charAt(0)) + ".png" :
                user.getUid() + "/avatar/avatar" + user.getAvatarType();
        return remote + path;
    }

    /**
    * @Description: Get User's Image from Image Server
    */
    public static String getUserAvatar(long uid, String type){
        String path = uid + "/avatar/avatar" + type;
        return remote + path;
    }

    /**
    * @Description: Get file's type
    */
    public static String getType(String fileName){
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    /**
    * @Description: Get property cover from image server.
    */
    public static List<String> getImages(long pid){
        RestTemplate restTemplate = new RestTemplate();
        String[] names = restTemplate.getForObject(remote + "cover" + "/" + pid, String[].class);
        List<String> paths = new ArrayList<>();
        if(names !=null) {
            for (String name : names) {
                paths.add(remote + "property/" + pid + "/" + name);
            }
        }
        return paths;
    }
}