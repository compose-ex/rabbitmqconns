#!/usr/bin/env python
import pika
import sys
import ssl

try:
    from urllib import urlencode
except ImportError:
    from urllib.parse import urlencode

ssl_opts = urlencode(
    {'ssl_options': {'ca_certs': './composecert', 'cert_reqs': ssl.CERT_REQUIRED}})
full_url = 'amqps://[user]:[password]@aws-eu-west-1-portal.1.dblayer.com:11020/tangy-rabbitmq-80?' + ssl_opts
parameters = pika.URLParameters(full_url)

connection = pika.BlockingConnection(parameters)
channel = connection.channel()

message='This is not a message, this is a tribute to a message'
my_routing_key='tributes'
exchange_name='postal'

channel.exchange_declare(exchange=exchange_name,
                         type='direct',
                         durable=True)


channel.basic_publish(exchange=exchange_name,
                      routing_key=my_routing_key,
                      body=message)

channel.close()
connection.close()
