package com.lawsofnature.rabbitmq

import java.util.concurrent.{ExecutorService, Executors}

import com.rabbitmq.client.{Channel, Connection, ConnectionFactory, MessageProperties}

/**
  * Created by fangzhongwei on 2016/10/22.
  */
object Producer extends App {
  var factory: ConnectionFactory = new ConnectionFactory()
  factory.setUsername("fangzhongwei")
  factory.setPassword("fzw@2016")
  factory.setVirtualHost("/")
  factory.setHost("192.168.181.130")
  factory.setPort(5672)
  var service: ExecutorService = Executors.newFixedThreadPool(500)
  factory.setSharedExecutor(service)
  //共用connection
  var conn: Connection = factory.newConnection()
  var channel: Channel = conn.createChannel()

  channel.exchangeDeclare("firstExchange", "direct") //direct fanout topic
  channel.queueDeclare("firstQueue", false, false, false, null)
  channel.queueDeclare("secondQueue", false, false, false, null)
  channel.queueBind("firstQueue", "firstExchange", "routingKey")
  channel.queueBind("secondQueue", "firstExchange", "routingKey2")

  //需要绑定路由键
  channel.basicPublish("firstExchange", "routingKey", MessageProperties.PERSISTENT_TEXT_PLAIN, "hello first192".getBytes("UTF-8"))
  channel.basicPublish("firstExchange", "routingKey2", MessageProperties.PERSISTENT_TEXT_PLAIN, "hello second192".getBytes("UTF-8"))

  println("publish finish")

  channel.close()
  conn.close()
}
