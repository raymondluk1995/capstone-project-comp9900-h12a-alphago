package alphago.propertysale.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: propertysale
 * @description: A message producer to send messages into Message Queue
 *               The type of the message queue is Routing
 *               The Fanout message queue model is used in the system, where an exchanger tied with different
 *               queues Distinguished by routing keys.
 **/


@Component
public class MessageProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.myExchange}")
    private String exchange;

    /**
    * @Description: Send message to Exchanger based on routingKey
    * @Param: msg: Message need to be sent
    *         routingKey: Identifier of queue
    */
    public void sendMsg(Object msg, String routingKey) {
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(exchange, routingKey, msg);
    }

}