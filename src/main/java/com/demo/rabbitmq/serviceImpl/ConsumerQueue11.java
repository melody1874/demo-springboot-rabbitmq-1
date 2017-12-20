package com.demo.rabbitmq.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.demo.rabbitmq.pojo.Result;
import com.demo.rabbitmq.pojo.ShortMessage;
import com.demo.rabbitmq.util.MyLog;
import com.rabbitmq.client.Channel;


@Service
@RabbitListener(
		containerFactory="rabbitListenerContainerFactory",
		queues={"${mq.demo.queue1}","${mq.demo.queue2}"})
public  class ConsumerQueue11  {
	static Logger logger = LoggerFactory.getLogger(ConsumerQueue11.class);
	
	@Autowired
	DealMethod dm;
	
	@RabbitHandler
    public void process(
    		@Header("amqp_deliveryTag") long deliveryTag,
    		@Payload ShortMessage shortMessage,
    		Channel channel) {
		try {
			Result result = dm.method(shortMessage);				
			//处理消息完毕,写入日志
			MyLog.subInfo(shortMessage, result, result.isResult());
			channel.basicAck(deliveryTag, false);
		} catch (Exception e) {
			
		}
    }
}