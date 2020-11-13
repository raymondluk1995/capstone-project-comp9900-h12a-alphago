package alphago.propertysale.rabbit;

import alphago.propertysale.utils.CheckCode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @program: propertysale
 * @description: A message producer to send messages into Message Queue
 *               The type of the message queue is Routing
 * @author: XIAO HAN
 * @create: 2020-10-08 19:56
 **/


@Component
public class MessageProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Value("${spring.rabbitmq.myExchange}")
    private String exchange;

    public void sendMsg(Object msg, String routingKey) {
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(exchange, routingKey, msg);
    }

}