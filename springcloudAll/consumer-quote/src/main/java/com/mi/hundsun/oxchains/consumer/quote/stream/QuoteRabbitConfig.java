package com.mi.hundsun.oxchains.consumer.quote.stream;

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
 * Rabbit配置
 */
@Configuration
public class QuoteRabbitConfig {
    @Value("${spring.rabbitmq.bianDepthExchange:}" + DigiccyMessageSource.DEPTH_OUTPUT_BIAN)
    private String bianDepthExchange;
    @Value("${spring.rabbitmq.bianDepthQueue:}" + DigiccyMessageSource.DEPTH_OUTPUT_BIAN_QUEUE)
    private String bianDepthQueue;

    //交换机
    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(bianDepthExchange);
    }

    //交易子委托队列
    @Bean
    public Queue waitSendBianDepthQueue() {
        return new Queue(bianDepthQueue);
    }

    //交易子委托队列绑定交换机
    @Bean
    public Binding bindingUser() {
        return BindingBuilder.bind(waitSendBianDepthQueue()).to(defaultExchange()).with(bianDepthQueue);
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
