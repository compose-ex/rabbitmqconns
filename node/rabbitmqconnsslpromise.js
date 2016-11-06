#!/usr/bin/env node

var amqp = require('amqplib');
var when = require('when');
var url = require('url');

rabbitmqurl = 'amqps://user:password@portal194-1.rabbity.compose-3.composedb.com:10194/Rabbity';
parsedurl = url.parse(rabbitmqurl);

amqp.connect(rabbitmqurl, { servername: parsedurl.hostname }).then(function(conn) {
    return when(conn.createChannel().then(function(channel) {
        var message = "This is not a message, this is a node tribute to a message";
        var routingKey = "tributes";
        var exchangeName = "postal";
        var ok=channel.assertExchange(exchangeName, "direct", { durable: true });
        return ok.then(function(_qok) {
            channel.publish(exchangeName, routingKey, new Buffer(message));
            return;
        });
    })).ensure(function() { conn.close(); });
}).catch(console.warn);
