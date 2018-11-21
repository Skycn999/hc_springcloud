package com.mi.hundsun.oxchains.provider.trade.rabbitmq;

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
public class RabbitConfig {
    @Value("${spring.rabbitmq.txSyncExchange:txSyncExchange}")
    private String txExchange;
    @Value("${spring.rabbitmq.subDelegateQueue:subDelegateQueue}")
    private String subDelegateQueue;
    @Value("${spring.rabbitmq.subDelegateFailureQueue:sub-delegate-failure-queue}")
    private String subDelegateFailureQueue;
    @Value("${spring.rabbitmq.subDelegateSuccessQueue:sub-delegate-success-queue}")
    private String subDelegateSuccessQueue;
    @Value("${spring.rabbitmq.revokeDelegateFailureQueue:revoke-delegate-failure-queue}")
    private String revokeDelegateFailureQueue;
    @Value("${spring.rabbitmq.mainDelegateFailureQueue:main-delegate-failure-queue}")
    private String mainDelegateFailureQueue;

    //交换机
    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(txExchange);
    }

    //交易子委托队列
    @Bean
    public Queue waitSendSubDelegateQueue() {
        return new Queue(subDelegateQueue);
    }

    //交易子委托队列绑定交换机
    @Bean
    public Binding bindingSubDelegate() {
        return BindingBuilder.bind(waitSendSubDelegateQueue()).to(defaultExchange()).with(subDelegateQueue);
    }


    @Bean
    public Queue waitSendSubDelegateFailureQueue() {
        return new Queue(subDelegateFailureQueue);
    }
    @Bean
    public Binding bindingSubDelegateFailure() {
        return BindingBuilder.bind(waitSendSubDelegateFailureQueue()).to(defaultExchange()).with(subDelegateFailureQueue);
    }

    @Bean
    public Queue waitSendSubDelegateSuccessQueue() {
        return new Queue(subDelegateSuccessQueue);
    }
    //交易子委托队列绑定交换机
    @Bean
    public Binding bindingSubDelegateSuccess() {
        return BindingBuilder.bind(waitSendSubDelegateFailureQueue()).to(defaultExchange()).with(subDelegateSuccessQueue);
    }

    @Bean
    public Queue waitSendRevokeDelegateFailureQueue() {
        return new Queue(revokeDelegateFailureQueue);
    }
    //交易子委托队列绑定交换机
    @Bean
    public Binding bindingRevokeDelegateSuccess() {
        return BindingBuilder.bind(waitSendSubDelegateFailureQueue()).to(defaultExchange()).with(revokeDelegateFailureQueue);
    }


    @Bean
    public Queue waitSendMainDelegateFailureQueue() {
        return new Queue(mainDelegateFailureQueue);
    }
    //交易子委托队列绑定交换机
    @Bean
    public Binding bindingMainDelegateFailure() {
        return BindingBuilder.bind(waitSendMainDelegateFailureQueue()).to(defaultExchange()).with(mainDelegateFailureQueue);
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

    public interface RabbitMqMessageSource {


        /**
         * 子委托成功处理队列
         */
        String SUB_DELEGATE_SUCCESS_QUEUE = "sub-delegate-success-queue";

        /**
         * 子委托失败处理队列
         */
        String SUB_DELEGATE_FAILURE_QUEUE = "sub-delegate-failure-queue";

        /**
         * 主委托失败处理队列
         */
        String MAIN_DELEGATE_FAILURE_QUEUE = "main-delegate-failure-queue";
        String REVOKE_DELEGATE_FAILURE_QUEUE = "revoke-delegate-failure-queue";


    }
}
