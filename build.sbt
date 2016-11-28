name := """apigateway"""

version := "1.0"

scalaVersion := "2.12.0"

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.36",
  "com.typesafe.akka" % "akka-actor_2.11" % "2.4.14",

  //                      "com.typesafe.akka"    %%    "akka-http-spray-json-experimental"    % "2.4.14",
//  "com.typesafe.akka" %% "akka-http-jackson" % "10.0.0",
  "com.typesafe.akka" % "akka-stream_2.11" % "2.4.14",
  "com.typesafe.akka" % "akka-http-core_2.11" % "10.0.0",
  "com.typesafe.akka" % "akka-http_2.11" % "10.0.0",
  "net.codingwell" % "scala-guice_2.11" % "4.1.0"
)

libraryDependencies += "com.zeroc" % "ice" % "3.6.2"

libraryDependencies += "com.lawsofnature.member" % "memberclient_2.11" % "1.0-SNAPSHOT"
libraryDependencies += "com.lawsofnature.logback" % "kafka-logback_2.11" % "1.0-SNAPSHOT"
libraryDependencies += "com.lawsofnature.common" % "common-error_2.11" % "1.0"
libraryDependencies += "com.lawsofnature.common" % "common-edecrypt_2.11" % "1.0"
libraryDependencies += "com.lawsofnature.client" % "ssoclient_2.11" % "1.0"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.7"
// https://mvnrepository.com/artifact/com.rabbitmq/amqp-client
libraryDependencies += "com.rabbitmq" % "amqp-client" % "3.6.5"

// https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-scala_2.11
libraryDependencies += "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.8.3"

// https://mvnrepository.com/artifact/commons-lang/commons-lang
libraryDependencies += "commons-lang" % "commons-lang" % "2.6"

// https://mvnrepository.com/artifact/org.scala-lang/scala-library
libraryDependencies += "org.scala-lang" % "scala-library" % "2.12.0"

