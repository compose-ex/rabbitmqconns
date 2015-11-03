require 'bunny'

conn = Bunny.new('amqps://[user]:[password]@aws-eu-west-1-portal.1.dblayer.com:11020/tangy-rabbitmq-80',
                 verify_peer: true,
                 tls_ca_certificates: ['./composecert'])
conn.start

ch = conn.create_channel

message = 'This is not a message, this is a tribute to a message'
routingKey = 'tributes'
exchangeName = 'postal'

x = ch.direct(exchangeName, durable: true)

x.publish(message, routing_key: routingKey)

ch.close
conn.close
