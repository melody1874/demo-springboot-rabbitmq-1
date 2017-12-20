package com.demo.rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.demo.rabbitmq.pojo.SimpleMessage;
import com.demo.rabbitmq.service.CrmSendService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=DemoRabbitMqApp.class)
public class CrmSendServiceImplTest {

	@Autowired
	CrmSendService se;
	
	@Test
	public void produce(){
		String content = "wa ha ha";
		int type = 11;
		int type1 = 12;
		
		SimpleMessage msg = new SimpleMessage();
		msg.setContent(content);
		msg.setType(type1);
		out(msg);
		try {
			out(se.produceMsg(msg));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void out(Object object){
		System.out.println("***********************结果***************************");
		System.out.println(JSONObject.toJSONString(object));
	}
}
