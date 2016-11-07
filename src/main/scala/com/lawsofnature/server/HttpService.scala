package com.lawsofnature.server

import java.util

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import akka.stream.ActorMaterializer
import com.google.inject.name.Names
import com.google.inject.{AbstractModule, Guice}
import com.lawsofnatrue.common.ice.{ConfigHelper, IcePrxFactory, IcePrxFactoryImpl}
import com.lawsofnature.action.{RegisterAction, RegisterActionImpl}
import com.lawsofnature.common.exception.ServiceException
import com.lawsofnature.factory.ResponseFactory
import com.lawsofnature.member.client.{MemberClientService, MemberClientServiceImpl}
import com.lawsofnature.service.{MemberService, MemberServiceImpl}
import com.typesafe.config.ConfigFactory
import org.slf4j.{Logger, LoggerFactory}

object HttpService extends App {
  val logger: Logger = LoggerFactory.getLogger(getClass)

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
      bind(classOf[RegisterAction]).to(classOf[RegisterActionImpl]).asEagerSingleton()

      //      bindInterceptor(Matchers.subclassesOf(), Matchers.annotatedWith(classOf[ExceptionInterceptor]), inte )
    }
  })

  //  injector.getInstance(classOf[MemberClientService]).initClient

  implicit val system: ActorSystem = ActorSystem()

  implicit val materializer = ActorMaterializer()

  implicit val dispatcher = system.dispatcher

  val host: String = conf.getString("http.host")
  val port: Int = conf.getInt("http.port")


  implicit def myExceptionHandler: ExceptionHandler = ExceptionHandler {
    case ex: ServiceException =>
      logger.error("ServiceException", ex)
      ex.printStackTrace()
      println("ServiceExceptionServiceExceptionServiceException")
      ResponseFactory.serviceErrorResponse(ex.serviceErrorCode)
    case ex: Exception =>
      logger.error("internal Exception", ex)
      extractUri { uri =>
        println(s"Request to $uri could not be handled normally")
        ResponseFactory.commonErrorResponse()
      }
  }

  Http().bindAndHandle(injector.getInstance(classOf[Routes]).apigatewayRoutes, host, port)

  logger.info("http server start up at " + port)
}
