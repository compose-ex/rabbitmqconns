#!/usr/bin/env node

var amqp = require('amqplib/callback_api');
var url = require('url');

function bail(err, conn) {
    console.error(err);
    if (conn) conn.close(function() {
        process.exit(1);
    });
}

rabbitmqurl = 'amqps://user:password@portal194-1.rabbity.compose-3.composedb.com:10194/Rabbity';
parsedurl = url.parse(rabbitmqurl);

amqp.connect(rabbitmqurl, { servername: parsedurl.hostname }, function(err, conn) {
    if (err !== null) return bail(err, conn);
    conn.createChannel(function(err, channel) {
        if (err !== null) return bail(err, conn);
        var message = "This is not a message, this is a node tribute to a message";
        var routingKey = "tributes";
        var exchangeName = "postal";
        channel.assertExchange(exchangeName, "direct", {
            durable: true
        }, function(err, ok) {
            if (err !== null) return bail(err, conn);
            channel.publish(exchangeName, routingKey, new Buffer(message))
        });
    });
    setTimeout(function() { conn.close(); process.exit(0) }, 500);
});
