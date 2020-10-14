package alphago.propertysale.rabbit;

import alphago.propertysale.utils.ImageUtil;
import alphago.propertysale.entity.ImgPorter;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @program: image_server
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-13 22:43
 **/
@Component
@RabbitListener(bindings = @QueueBinding(value = @Queue(name = "imageSaver" , autoDelete = "false") ,
        exchange = @Exchange(name = "${spring.rabbitmq.myExchange}"),key = {"image"}))
public class ImageSaver {
    @RabbitHandler
    public void saveImage(@Payload ImgPorter[] imgPorters){
        for(ImgPorter imgPorter : imgPorters){
            ImageUtil.saveImage(imgPorter);
        }
    }
}