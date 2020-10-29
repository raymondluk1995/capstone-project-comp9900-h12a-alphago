package alphago.propertysale.rabbit;

import alphago.propertysale.utils.ImageUtil;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @program: image_server
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-23 13:52
 **/
@Component
@RabbitListener(bindings = @QueueBinding(value = @Queue(name = "imageRemover" , autoDelete = "false") ,
        exchange = @Exchange(name = "${spring.rabbitmq.myExchange}"),key = {"remove"}))
public class ImageRemover {

    @RabbitHandler
    public void remove(String pid){
        ImageUtil.remove(pid);
    }
}