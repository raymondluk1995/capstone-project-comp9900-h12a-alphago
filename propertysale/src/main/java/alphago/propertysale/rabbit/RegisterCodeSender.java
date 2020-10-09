package alphago.propertysale.rabbit;

import alphago.propertysale.utils.CheckCode;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @program: propertysale
 * @description: send message based on routing key
 * @author: XIAO HAN
 * @create: 2020-10-08 20:24
 **/
@Component
@RabbitListener(bindings = @QueueBinding(value = @Queue(name = "registerSender" , autoDelete = "false") ,
        exchange = @Exchange(name = "${spring.rabbitmq.myExchange}"),key = {CheckCode.REGISTER}))
public class RegisterCodeSender {
    @Autowired
    private JavaMailSender mailSender;

    @RabbitHandler
    public void sendValidationCode(String email){
        CheckCode.sendCode(mailSender , email , CheckCode.REGISTER);
    }
}