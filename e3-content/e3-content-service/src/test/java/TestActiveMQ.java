import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class TestActiveMQ {
	
	@Test
	public void testCreateQueueProducter() {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.190:61616");
		try {
			Connection connection = connectionFactory.createConnection();
			connection.start();
			// 第一个参数表示是否开启事务，第二个参数当第一个为false的时候有意义
			// 表示自动接收消息还是手动
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// 创建点对点的目的地
			Queue queue = session.createQueue("test-queue");
			// 创建生产者
			MessageProducer producer = session.createProducer(queue);
			TextMessage textMessage = session.createTextMessage();
			textMessage.setText("Hello ActiveMQ");
			producer.send(textMessage);
			//关闭资源
			producer.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateQueueConsumer() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.190:61616");
		try {
			Connection connection = connectionFactory.createConnection();
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue("test-queue");
			MessageConsumer consumer = session.createConsumer(queue);
			consumer.setMessageListener(new MessageListener() {
				
				@Override
				public void onMessage(Message message) {
					TextMessage textMessage = (TextMessage) message;
					try {
						System.out.println(textMessage.getText());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
			
			System.out.println("开始");
			System.in.read();
			System.out.println("结束");
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void createTopic() {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.190:61616");
		try {
			Connection connection = connectionFactory.createConnection();
			connection.start();
			// 第一个参数表示是否开启事务，第二个参数当第一个为false的时候有意义
			// 表示自动接收消息还是手动
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// 创建点对点的目的地
			Topic topic = session.createTopic("test-topic");
			// 创建生产者
			MessageProducer producer = session.createProducer(topic);
			TextMessage textMessage = session.createTextMessage();
			textMessage.setText("Hello ActiveMQ");
			producer.send(textMessage);
			//关闭资源
			producer.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void createTopicConsumer() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.190:61616");
		try {
			Connection connection = connectionFactory.createConnection();
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic("test-topic");
			MessageConsumer consumer = session.createConsumer(topic);
			consumer.setMessageListener(new MessageListener() {
				
				@Override
				public void onMessage(Message message) {
					TextMessage textMessage = (TextMessage) message;
					try {
						System.out.println(textMessage.getText());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
			
			System.out.println("开始3");
			System.in.read();
			System.out.println("结束");
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
