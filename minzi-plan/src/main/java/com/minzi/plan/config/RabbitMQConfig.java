package com.minzi.plan.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue requestLogMqQueue(){
        return new Queue("requestLogMqQueue",true);
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("direct_exchange");
    }

    @Bean
    public Binding binding(Queue requestLogMqQueue,DirectExchange directExchange){
        return BindingBuilder.bind(requestLogMqQueue).to(directExchange).with("requestLogKey");
    }
}
