name := """apigateway"""

version := "1.0"

javacOptions ++= Seq("-encoding", "UTF-8")

organization := "com.jxjxgo.apigateway"

scalaVersion := "2.11.8"

mainClass in (Compile, run) := Some("com.jxjxgo.apigateway.server.HttpService")

libraryDependencies ++= Seq(
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

// https://mvnrepository.com/artifact/com.trueaccord.scalapb/scalapb-runtime_2.11
libraryDependencies += "com.trueaccord.scalapb" % "scalapb-runtime_2.11" % "0.5.46"

libraryDependencies += "com.jxjxgo.common" % "common-error_2.11" % "1.0"
libraryDependencies += "com.jxjxgo.common" % "common-edecrypt_2.11" % "1.0"
libraryDependencies += "com.jxjxgo.sms" % "smscommon_2.11" % "1.0"
libraryDependencies += "com.jxjxgo.i18n" % "i18ncommonlib_2.11" % "1.0"

libraryDependencies += "com.jxjxgo.sso" % "ssocommonlib_2.11" % "1.0"
libraryDependencies += "com.jxjxgo.common" % "common-utils_2.11" % "1.0"

libraryDependencies += "com.jxjxgo.edcenter" % "edclient_2.11" % "1.0"
libraryDependencies += "com.jxjxgo.member" % "membercommonlib_2.11" % "1.0"
libraryDependencies += "com.jxjxgo.account" % "accountcommonlib_2.11" % "1.0"
libraryDependencies += "com.jxjxgo.gamecenter" % "gamecommonlib_2.11" % "1.0"





