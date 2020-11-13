package alphago.propertysale.rabbit;

import alphago.propertysale.utils.CheckCode;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @program: propertysale
 * @description: Send Reset Code
 * @author: XIAO HAN
 * @create: 2020-10-08 21:03
 **/
@Component
@RabbitListener(bindings = @QueueBinding(value = @Queue(name = "resetSender" , autoDelete = "false") ,
        exchange = @Exchange(name = "${spring.rabbitmq.myExchange}"),key = {CheckCode.RESET}))
public class RestCodeSender {
    @Autowired
    private JavaMailSender mailSender;

    @RabbitHandler
    public void sendValidationCode(String email){
        CheckCode.sendCode(mailSender , email , CheckCode.RESET);
    }
}