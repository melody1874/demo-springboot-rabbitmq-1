package com.demo.rabbitmq.producerImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.demo.rabbitmq.pojo.Result;
import com.demo.rabbitmq.pojo.ShortMessage;
import com.demo.rabbitmq.producer.SimpleProducer;
import com.demo.rabbitmq.util.Cont;
import com.demo.rabbitmq.util.MyLog;

@Service
public class SimpleProducerImpl implements SimpleProducer {
	static Logger logger = LoggerFactory.getLogger(SimpleProducerImpl.class);

	@Value("${mq.demo.topic.exchange1}")
	public String topicExchangeName1;
	@Value("${mq.demo.queue1}")
	public String queueName1;	
	@Value("${mq.demo.queue2}")
	public String queueName2;
	
	@Autowired
	MqSendMessage mqSendMessage;
	
	public Result sendMessage(ShortMessage shortMessage) {
		Result result=new Result();		
		try {			
			deal(shortMessage);	
			result.setResult(true);
			result.setCode(Cont.CODE_SUCCESS);
			result.setData(shortMessage.getIe());
			result.setMessage("推送消息成功");
			MyLog.pubInfo(shortMessage, null);
		} catch (Exception e) {
			result.setResult(false);
			result.setCode(Cont.CODE_FAILURE);
			result.setMessage("推送消息失败！");
			result.setError(e.getMessage() );
			MyLog.pubInfo(shortMessage, e);
		}
		return result;
	}
	
	//处理
	public boolean deal(ShortMessage shortMessage) throws Exception{
		boolean rs = false;		
		switch (shortMessage.getType()) {
			case 11:
				rs = mqSendMessage.mqSendMessage(topicExchangeName1, "demo11.test", shortMessage);
				break;
			case 12:
				rs = mqSendMessage.mqSendMessage(topicExchangeName1, "demo12.test", shortMessage);
				break;
			default:
				throw new Exception("type值不匹配");
		}
		return rs;
	}

	@Override
	public Result produceMsg(ShortMessage msg) {
		return sendMessage(msg);
	}
	

}
