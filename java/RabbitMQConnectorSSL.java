package rabbitmqconnector;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class RabbitMQConnectorSSL {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            char[] keyPassPhrase = "ilikeamiga".toCharArray();

            KeyStore tks = KeyStore.getInstance("JKS");
            tks.load(new FileInputStream("./rabbitstore"), keyPassPhrase);            
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(tks);

            SSLContext c = SSLContext.getInstance("TLS");
            c.init(null, tmf.getTrustManagers(), null);

            ConnectionFactory factory = new ConnectionFactory();

            factory.setUri("amqps://dj:admin@aws-eu-west-1-portal.1.dblayer.com:11020/tangy-rabbitmq-80");
            factory.useSslProtocol(c);

            Connection conn = factory.newConnection();
            

            Channel channel = conn.createChannel();

            String message = "This is not a message, this is a tribute to a message";
            String routingKey = "tributes";
            String exchangeName = "postal";

            channel.exchangeDeclare(exchangeName, "direct", true);

            String queueName = channel.queueDeclare().getQueue();

            channel.queueBind(queueName, exchangeName, routingKey);

            channel.basicPublish(exchangeName, routingKey, null, message.getBytes());

            channel.close();
            conn.close();
        } catch (URISyntaxException | NoSuchAlgorithmException | KeyManagementException | IOException | CertificateException | KeyStoreException | TimeoutException ex) {
            Logger.getLogger(RabbitMQConnectorSSL.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
