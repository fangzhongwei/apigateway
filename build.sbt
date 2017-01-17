name := """apigateway"""

version := "1.0"

javacOptions ++= Seq("-encoding", "UTF-8")

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.36",
  "com.rabbitmq" % "amqp-client" % "3.6.5",
  "com.zeroc" % "ice" % "3.6.2",
  "com.typesafe.akka" %% "akka-actor" % "2.4.14",
  "com.typesafe.akka" %% "akka-stream" % "2.4.14",
  "com.typesafe.akka" % "akka-http-core_2.11" % "10.0.0",
  "com.typesafe.akka" % "akka-http_2.11" % "10.0.0",
  "net.codingwell" % "scala-guice_2.11" % "4.1.0",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.8.4",
  "commons-lang" % "commons-lang" % "2.6",
  "javax.validation" % "validation-api" % "1.1.0.Final",
  "com.google.protobuf" % "protobuf-java" % "3.0.0",
  "org.scala-lang" % "scala-library" % "2.11.8"
)

libraryDependencies += "com.lawsofnature.member" % "memberclient_2.11" % "1.0-SNAPSHOT"
libraryDependencies += "com.lawsofnature.logback" % "kafka-logback_2.11" % "1.0-SNAPSHOT"
libraryDependencies += "com.lawsofnature.common" % "common-error_2.11" % "1.0"
libraryDependencies += "com.lawsofnature.common" % "common-edecrypt_2.11" % "1.0"
libraryDependencies += "com.lawsofnature.client" % "ssoclient_2.11" % "1.0"
libraryDependencies += "com.lawsofnature.common" % "common-utils_2.11" % "1.0"
libraryDependencies += "com.lawsofnature.sms" % "smsclient_2.11" % "1.0"
libraryDependencies += "com.lawsofnature.edcenter" % "edclient_2.11" % "1.0"
libraryDependencies += "com.lawsofnature.i18n" % "i18nclient_2.11" % "1.0"
libraryDependencies += "com.lawsofnature.account" % "accountclient_2.11" % "1.0"

// https://mvnrepository.com/artifact/com.trueaccord.scalapb/scalapb-runtime_2.11
libraryDependencies += "com.trueaccord.scalapb" % "scalapb-runtime_2.11" % "0.5.46"




