package alphago.propertysale.rabbit;

import alphago.propertysale.Utils.ImageUtil;
import alphago.propertysale.entity.AvatarPorter;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


/**
 * @program: image_server
 * @description: Save Users' Avatar
 * @author: XIAO HAN
 * @create: 2020-10-08 23:26
 **/
@Component
@RabbitListener(bindings = @QueueBinding(value = @Queue(name = "avatarSaver" , autoDelete = "false") ,
        exchange = @Exchange(name = "${spring.rabbitmq.myExchange}"),key = {"avatar"}))
public class AvatarSaver {
    @RabbitHandler
    public void saveAvatar(@Payload AvatarPorter porter) throws Exception {
        ImageUtil.AvatarSave(porter);
    }
}