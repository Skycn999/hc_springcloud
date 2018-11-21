package com.mi.hundsun.oxchains.consumer.web.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by houbin20111 on 2017/3/29. Rabbit配置
 */
@Configuration
public class WebRabbitConfig {

    @Value("${spring.rabbitmq.userExchange:userExchange}")
    private String userExchange;
    @Value("${spring.rabbitmq.userQueue:userQueue}")
    private String userQueue;

    @Value("${spring.rabbitmq.txExchange:txExchange}")
    private String txExchange;
    @Value("${spring.rabbitmq.txSellOutQueue:txSellOutQueue}")
    private String txSellOutQueue;
    @Value("${spring.rabbitmq.txBuyInQueue:txBuyInQueue}")
    private String txBuyInQueue;




    //交换机
    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(userExchange);
    }
    //交换机
    @Bean
    public DirectExchange txExchange() {
        return new DirectExchange(txExchange);
    }

    //交易子委托队列
    @Bean
    public Queue waitSendUserQueue() {
        return new Queue(userQueue);
    }

    //交易子委托队列绑定交换机
    @Bean
    public Binding bindingUser() {
        return BindingBuilder.bind(waitSendUserQueue()).to(defaultExchange()).with(userQueue);
    }

    //交易子委托队列
    @Bean
    public Queue waitSendTxBuyInQueue() {
        return new Queue(txBuyInQueue);
    }
    //交易子委托队列
    @Bean
    public Queue waitSendTxSellOutQueue() {
        return new Queue(txSellOutQueue);
    }
    //交易子委托队列绑定交换机
    @Bean
    public Binding bindingTxBuyIn() {
        return BindingBuilder.bind(waitSendTxBuyInQueue()).to(txExchange()).with(txBuyInQueue);
    }
    @Bean
    public Binding bindingTxSellOut() {
        return BindingBuilder.bind(waitSendTxSellOutQueue()).to(txExchange()).with(txSellOutQueue);
    }



    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }
}
