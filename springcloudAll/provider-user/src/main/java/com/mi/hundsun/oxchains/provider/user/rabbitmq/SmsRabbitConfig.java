package com.mi.hundsun.oxchains.provider.user.rabbitmq;

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
public class SmsRabbitConfig {
    @Value("${spring.rabbitmq.smsExchange:smsExchange}")
    private String smsExchange;
    @Value("${spring.rabbitmq.smsQueue:smsQueue}")
    private String smsQueue;
    @Value("${spring.rabbitmq.emailQueue:emailQueue}")
    private String emailQueue;
    @Value("${spring.rabbitmq.insideQueue:insideQueue}")
    private String insideQueue;
    @Value("${spring.rabbitmq.inside2Queue:inside2Queue}")
    private String inside2Queue;

    //交换机
    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(smsExchange);
    }

    //短信发送队列
    @Bean
    public Queue waitSendSmsQueue() {
        return new Queue(smsQueue);
    }

    //短信发送队列绑定交换机
    @Bean
    public Binding bindingSms() {
        return BindingBuilder.bind(waitSendSmsQueue()).to(defaultExchange()).with(smsQueue);
    }

    //邮件发送队列
    @Bean
    public Queue waitSendEmailQueue() {
        return new Queue(emailQueue);
    }

    //邮件发送队列绑定交换机
    @Bean
    public Binding bindingEmail() {
        return BindingBuilder.bind(waitSendEmailQueue()).to(defaultExchange()).with(emailQueue);
    }

    //站内信发送队列
    @Bean
    public Queue waitSendInsideQueue() {
        return new Queue(insideQueue);
    }

    //站内信发送队列绑定交换机
    @Bean
    public Binding bindingInside() {
        return BindingBuilder.bind(waitSendInsideQueue()).to(defaultExchange()).with(insideQueue);
    }


    //站内信2发送队列
    @Bean
    public Queue waitSendInside2Queue() {
        return new Queue(inside2Queue);
    }

    //站内信2发送队列绑定交换机
    @Bean
    public Binding bindingInside2() {
        return BindingBuilder.bind(waitSendInside2Queue()).to(defaultExchange()).with(inside2Queue);
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
