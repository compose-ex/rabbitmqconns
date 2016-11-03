package rabbitmqconnector;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RabbitMQConnector {
  public static void main(String[] args) {
  	try {
  		ConnectionFactory factory = new ConnectionFactory();
  		factory.setUri( "amqps://user:password@portal194-1.rabbity.compose-3.composedb.com:10194/Rabbity");

      Connection conn = factory.newConnection();

  		Channel channel = conn.createChannel();

      String	message = "This is not a message, this is a tribute to a message";
  		String	routingKey = "tributes";
  		String	exchangeName = "postal";

  		channel.exchangeDeclare(exchangeName, "direct", true);

  		String queueName = channel.queueDeclare().getQueue();

  		channel.queueBind(queueName, exchangeName, routingKey);

  		channel.basicPublish(exchangeName, routingKey, null, message.getBytes());

  		channel.close();
  		conn.close();
    } catch (IOException | TimeoutException | URISyntaxException | NoSuchAlgorithmException | KeyManagementException ex) {
          		Logger.getLogger(RabbitJava.class.getName()).log(Level.SEVERE, null, ex);
  	}
  }
}
