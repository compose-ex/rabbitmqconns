package main

import (
	"crypto/tls"
	"crypto/x509"
	"fmt"
	"io/ioutil"
	"log"

	"github.com/streadway/amqp"
)

func failOnError(err error, msg string) {
	if err != nil {
		log.Fatalf("%s: %s", msg, err)
		panic(fmt.Sprintf("%s: %s", msg, err))
	}
}

func main() {
	cfg := new(tls.Config)
	cfg.RootCAs = x509.NewCertPool()

	if ca, err := ioutil.ReadFile("composecert"); err == nil {
		cfg.RootCAs.AppendCertsFromPEM(ca)
	}
	conn, err := amqp.DialTLS("amqps://[user]:[password]@aws-eu-west-1-portal.1.dblayer.com:11020/tangy-rabbitmq-80", cfg)

	failOnError(err, "Failed to connect to RabbitMQ")
	defer conn.Close()

	message := "This is not a message, this is a tribute to a message"
	routingKey := "tributes"
	exchangeName := "postal"

	ch, err := conn.Channel()
	failOnError(err, "Failed to open a channel")
	defer ch.Close()

	err = ch.ExchangeDeclare(
		exchangeName, // name
		"direct",     // type
		true,         // durable
		false,        // auto-deleted
		false,        // internal
		false,        // no-wait
		nil,          // arguments
	)
	failOnError(err, "Failed to declare an exchange")

	err = ch.Publish(
		exchangeName, // exchange
		routingKey,   // routing key
		false,        // mandatory
		false,        // immediate
		amqp.Publishing{
			ContentType: "text/plain",
			Body:        []byte(message),
		})
	failOnError(err, "Failed to publish a message")
}
