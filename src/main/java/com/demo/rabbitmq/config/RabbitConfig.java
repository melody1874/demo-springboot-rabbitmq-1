package com.demo.rabbitmq.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;





@Configuration
public class RabbitConfig {
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
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }
	/************exchange***********************/
    @Bean(name="exchange1")
    public TopicExchange exchange1(@Value("${mq.demo.topic.exchange1}") String exchangeName){
    	//auto_delete: 当所有绑定队列都不再使用时，是否自动删除该交换机。
    	TopicExchange topicExchange = new TopicExchange(exchangeName,true,false);
    	return topicExchange;
    }
	 /************queue***********************/
    @Bean(name="queue11")
    public Queue queue1(@Value("${mq.demo.queue1}") String queueName){
    	//exclusive: 仅创建者可以使用的私有队列，断开后自动删除。
    	//auto_delete: 当所有消费客户端连接断开后，是否自动删除队列。
        Queue queue = new Queue(queueName,true,false,false);
        return queue;
    }
    @Bean(name="queue12")
    public Queue queue2(@Value("${mq.demo.queue2}") String queueName){
        Queue queue = new Queue(queueName,true,false,false);
        return queue;
    }    
	 /************bindings***********************/
    @Bean
    Binding binding1(@Qualifier("queue11") Queue queue,
    		@Qualifier("exchange1") TopicExchange exchange,
    		@Value("${mq.demo.queue1.routingKey}") String routingKey){
    	 Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
	     return binding;
    }
    @Bean
    Binding binding2(@Qualifier("queue12") Queue queue,
    		@Qualifier("exchange1") TopicExchange exchange,
    		@Value("${mq.demo.queue2.routingKey}") String routingKey){
    	 Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
	     return binding;
    }    
}
