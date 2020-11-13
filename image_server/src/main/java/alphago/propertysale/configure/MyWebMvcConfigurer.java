package alphago.propertysale.configure;

import alphago.propertysale.utils.ImageUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: image_server
 * @description:
 * @author: XIAO HAN
 * @create: 2020-11-07 19:44
 **/
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println(ImageUtil.PATH);
        registry.addResourceHandler("/**").addResourceLocations("file:"+ ImageUtil.PATH+"/");
    }
}