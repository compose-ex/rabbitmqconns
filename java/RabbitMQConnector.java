package rabbitmqconnector;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RabbitMQConnector {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ConnectionFactory factory=new ConnectionFactory();

            factory.setUri("amqps://dj:admin@aws-eu-west-1-portal.1.dblayer.com:11020/tangy-rabbitmq-80");
       
            Connection conn=factory.newConnection();
            
            Channel channel=conn.createChannel();
            
            String message="This is not a message, this is a tribute to a message";
            String routingKey="tributes";
            String exchangeName="postal";
            
            channel.exchangeDeclare(exchangeName,"direct",true);
         
            channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
            
            channel.close();
            conn.close();  
        } catch (URISyntaxException | NoSuchAlgorithmException | KeyManagementException | IOException | TimeoutException ex) {
            Logger.getLogger(RabbitMQConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
