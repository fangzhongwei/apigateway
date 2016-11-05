package com.lawsofnature.server

import java.util

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.google.inject.matcher.Matchers
import com.google.inject.name.Names
import com.google.inject.{AbstractModule, Guice, Inject}
import com.lawsofnatrue.common.ice.{ConfigHelper, IcePrxFactory, IcePrxFactoryImpl}
import com.lawsofnature.annotations.ExceptionInterceptor
import com.lawsofnature.member.client.{MemberClientService, MemberClientServiceImpl}
import com.lawsofnature.service.{MemberService, MemberServiceImpl}
import com.typesafe.config.ConfigFactory
import org.aopalliance.intercept.{MethodInterceptor, MethodInvocation}
import org.slf4j.{Logger, LoggerFactory}

object HttpService extends App {

  val LOGGER: Logger = LoggerFactory.getLogger(getClass)

  val conf = ConfigFactory.load()

//  var inte = new MethodInterceptor {
//    override def invoke(methodInvocation: MethodInvocation): AnyRef = {
//      var proceed: AnyRef = new AnyRef
//      try {
//
//         proceed = methodInvocation.proceed()
//      } catch {
//        case e:Exception => ""
//      }
//
//      proceed
//    }
//  }

  private val injector = Guice.createInjector(new AbstractModule() {
    override def configure() {
      val map: util.HashMap[String, String] = ConfigHelper.configMap
      Names.bindProperties(binder(), map)
      bind(classOf[MemberService]).to(classOf[MemberServiceImpl]).asEagerSingleton()
      bind(classOf[IcePrxFactory]).to(classOf[IcePrxFactoryImpl]).asEagerSingleton()
      bind(classOf[MemberClientService]).to(classOf[MemberClientServiceImpl]).asEagerSingleton()

//      bindInterceptor(Matchers.subclassesOf(), Matchers.annotatedWith(classOf[ExceptionInterceptor]), inte )
    }
  })

//  injector.getInstance(classOf[MemberClientService]).initClient

  implicit val system: ActorSystem = ActorSystem()

  implicit val materializer = ActorMaterializer()

  implicit val dispatcher = system.dispatcher

  val host: String = conf.getString("http.host")
  val port: Int = conf.getInt("http.port")

  Http().bindAndHandle(injector.getInstance(classOf[Routes]).apigatewayRoutes, host, port)

  LOGGER.info("http server start up at " + port)
}
