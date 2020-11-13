package alphago.propertysale.controller;

import alphago.propertysale.utils.ImageUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: image_server
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-14 11:54
 **/
@RestController
@RequestMapping("/img")
public class ImageController {
    @RequestMapping("/cover/{pid}")
    public String[] cover(@PathVariable int pid){
        return ImageUtil.getCover(pid);
    }
}