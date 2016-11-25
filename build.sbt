name := """apigateway"""

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++=  Seq(
                      "mysql"                %     "mysql-connector-java"     %      "5.1.36",
                      "com.typesafe.akka"    %%    "akka-actor"                           % "2.4.11",
                      "com.typesafe.akka"    %%    "akka-http-spray-json-experimental"    % "2.4.11",
                      "com.typesafe.akka"    %%    "akka-stream" % "2.4.11",
                      "com.typesafe.akka"    %%    "akka-http-core" % "2.4.11",
                      "org.scalatest"        %%    "scalatest"    	      %      "2.2.5"     %    "test",
                      "com.h2database"       % 	   "h2"                       %      "1.4.187",
                      "net.codingwell" %% "scala-guice" % "4.0.1",
                      "com.typesafe.akka" %% "akka-http-testkit-experimental" % "2.0.3" %    "test"
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
// https://mvnrepository.com/artifact/org.springframework/spring-core
//libraryDependencies += "org.springframework" % "spring-core" % "4.3.3.RELEASE"

// https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-scala_2.11
libraryDependencies += "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.8.3"

// https://mvnrepository.com/artifact/commons-lang/commons-lang
libraryDependencies += "commons-lang" % "commons-lang" % "2.6"
