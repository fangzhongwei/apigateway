package com.lawsofnature.server

import java.util
import java.util.Map.Entry

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import akka.stream.ActorMaterializer
import com.google.inject.matcher.Matchers
import com.google.inject.name.Names
import com.google.inject.{AbstractModule, Binding, Guice, Key}
import com.lawsofnatrue.common.ice.{ConfigHelper, IcePrxFactory, IcePrxFactoryImpl}
import com.lawsofnature.action.{RegisterAction, RegisterActionImpl}
import com.lawsofnature.annotations.ApiMapping
import com.lawsofnature.common.exception.ServiceException
import com.lawsofnature.factory.ResponseFactory
import com.lawsofnature.member.client.{MemberClientService, MemberClientServiceImpl}
import com.lawsofnature.service.{MemberService, MemberServiceImpl}
import com.typesafe.config.ConfigFactory
import org.aopalliance.intercept.{MethodInterceptor, MethodInvocation}
import org.slf4j.{Logger, LoggerFactory}

object HttpService extends App {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  val conf = ConfigFactory.load()

  var apiMethodInterceptor = new MethodInterceptor {
    override def invoke(methodInvocation: MethodInvocation): AnyRef = {
      var proceed: AnyRef = new AnyRef
      try {
        proceed = methodInvocation.proceed()
      } catch {
        case e: Exception => ""
      }
      proceed
    }
  }

   val injector = Guice.createInjector(new AbstractModule() {
    override def configure() {
      val map: util.HashMap[String, String] = ConfigHelper.configMap
      Names.bindProperties(binder(), map)
      bind(classOf[MemberService]).to(classOf[MemberServiceImpl]).asEagerSingleton()
      bind(classOf[IcePrxFactory]).to(classOf[IcePrxFactoryImpl]).asEagerSingleton()
      bind(classOf[MemberClientService]).to(classOf[MemberClientServiceImpl]).asEagerSingleton()
      bind(classOf[RegisterAction]).to(classOf[RegisterActionImpl]).asEagerSingleton()
      bindInterceptor(Matchers.any(), Matchers.annotatedWith(classOf[ApiMapping]), apiMethodInterceptor)
    }
  })

  private val bindings: util.Map[Key[_], Binding[_]] = injector.getBindings

  private val iterator: util.Iterator[Entry[Key[_], Binding[_]]] = bindings.entrySet().iterator()

  val actionBeanClassList = new util.ArrayList[Class[_]]()
  while (iterator.hasNext) {
    val entry: Entry[Key[_], Binding[_]] = iterator.next()
    val clazz: Class[_ <: Key[_]] = entry.getKey.getClass
    if (clazz.getPackage.getName.equalsIgnoreCase("com.lawsofnature.action")) {
      actionBeanClassList.add(clazz)
    }
  }
  println(actionBeanClassList)


  injector.getInstance(classOf[MemberClientService]).initClient

  implicit val system: ActorSystem = ActorSystem()

  implicit val materializer = ActorMaterializer()

  implicit val dispatcher = system.dispatcher

  val host: String = conf.getString("http.host")
  val port: Int = conf.getInt("http.port")


  implicit def myExceptionHandler: ExceptionHandler = ExceptionHandler {
    case ex: ServiceException =>
      logger.error("ServiceException", ex)
      println("ServiceExceptionServiceExceptionServiceException")
//      ResponseFactory.serviceErrorResponse(ex.serviceErrorCode)
      complete("internal error")
    case ex: Exception =>
      logger.error("internal Exception", ex)
      extractUri { uri =>
        println(s"Request to $uri could not be handled normally")
//        ResponseFactory.commonErrorResponse()
        complete("internal error")
      }
  }

  Http().bindAndHandle(injector.getInstance(classOf[Routes]).apigatewayRoutes, host, port)

  logger.info("http server start up at " + port)
}
