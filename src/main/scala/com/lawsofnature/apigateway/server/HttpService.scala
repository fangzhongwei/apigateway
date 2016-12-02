package com.lawsofnature.apigateway.server

import java.lang.reflect.Method
import java.util
import java.util.Map.Entry

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import akka.stream.ActorMaterializer
import com.google.inject.internal.LinkedBindingImpl
import com.google.inject.matcher.Matchers
import com.google.inject.name.Names
import com.google.inject.{AbstractModule, Binding, Guice, Key}
import com.lawsofnatrue.common.ice.{ConfigHelper, IcePrxFactory, IcePrxFactoryImpl}
import com.lawsofnature.apigateway.action.{RegisterAction, RegisterActionImpl, SSOAction, SSOActionImpl}
import com.lawsofnature.apigateway.service.{MemberService, MemberServiceImpl, SessionService, SessionServiceImpl}
import com.lawsofnature.common.exception.ServiceException
import com.lawsofnature.member.client.{MemberClientService, MemberClientServiceImpl}
import com.lawsofnature.sso.client.{SSOClientService, SSOClientServiceImpl}
import com.typesafe.config.ConfigFactory
import org.aopalliance.intercept.{MethodInterceptor, MethodInvocation}
import org.slf4j.{Logger, LoggerFactory}

object HttpService extends App {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  val conf = ConfigFactory.load()

  var apiMethodInterceptor = new MethodInterceptor {
    override def invoke(methodInvocation: MethodInvocation): AnyRef = {
      val millis: Long = System.currentTimeMillis()
      var name: String = null
      var proceed: AnyRef = new AnyRef
      try {
        val method1: Method = methodInvocation.getMethod
        name = method1.getDeclaringClass.toString + ":" + method1.getName
        proceed = methodInvocation.proceed()
      } catch {
        case e: Exception => logger.error("system", e)
          ""
      }
      logger.info("Call method " + name + " cost " + (System.currentTimeMillis() - millis))
      proceed
    }
  }

  val injector = Guice.createInjector(new AbstractModule() {
    override def configure() {
      val map: util.HashMap[String, String] = ConfigHelper.configMap
      Names.bindProperties(binder(), map)
      bind(classOf[MemberService]).to(classOf[MemberServiceImpl]).asEagerSingleton()
      bind(classOf[SessionService]).to(classOf[SessionServiceImpl]).asEagerSingleton()
      bind(classOf[IcePrxFactory]).to(classOf[IcePrxFactoryImpl]).asEagerSingleton()
      bind(classOf[MemberClientService]).to(classOf[MemberClientServiceImpl]).asEagerSingleton()
      bind(classOf[SSOClientService]).to(classOf[SSOClientServiceImpl]).asEagerSingleton()
      bind(classOf[RegisterAction]).to(classOf[RegisterActionImpl]).asEagerSingleton()
      bind(classOf[SSOAction]).to(classOf[SSOActionImpl]).asEagerSingleton()
      //      bindInterceptor(Matchers.any(), Matchers.annotatedWith(classOf[ApiMapping]), apiMethodInterceptor)
      bindInterceptor(Matchers.any(), Matchers.any(), apiMethodInterceptor)
    }
  })

  private val bindings: util.Map[Key[_], Binding[_]] = injector.getBindings

  private val iterator: util.Iterator[Entry[Key[_], Binding[_]]] = bindings.entrySet().iterator()

  val actionBeanClassList = new util.ArrayList[Class[_]]()
  while (iterator.hasNext) {
    val entry: Entry[Key[_], Binding[_]] = iterator.next()

    val clazz: Class[_ <: Key[_]] = entry.getKey.getClass
    val rawType: Class[_] = entry.getKey.getTypeLiteral.getRawType
    if (rawType.getName.startsWith(ConfigFactory.load().getString("pkg.action"))) {
      val value1: LinkedBindingImpl[_] = entry.getValue.asInstanceOf[LinkedBindingImpl[_]]
      val rawType1: Class[_] = value1.getLinkedKey.getTypeLiteral.getRawType
      actionBeanClassList.add(rawType1)
    }
  }

  com.lawsofnature.apigateway.invoker.ActionInvoker.initActionMap
  injector.getInstance(classOf[MemberClientService]).initClient
  injector.getInstance(classOf[SSOClientService]).initClient

  implicit val system: ActorSystem = ActorSystem("http-system")
  implicit val materializer = ActorMaterializer()
  implicit val dispatcher = system.dispatcher

  val host: String = conf.getString("http.host")
  val port: Int = conf.getInt("http.port")

  implicit def myExceptionHandler: ExceptionHandler = ExceptionHandler {
    case ex: ServiceException =>
      logger.error("ServiceException", ex)
      logger.error("ServiceExceptionServiceExceptionServiceException")
      //      ResponseFactory.serviceErrorResponse(ex.ErrorCode)
      complete("internal error")
    case ex: Exception =>
      logger.error("internal Exception", ex)
      extractUri { uri =>
        logger.error(s"Request to $uri could not be handled normally")
        //        ResponseFactory.commonErrorResponse()
        complete("invalid request")
      }
  }

  Http().bindAndHandle(injector.getInstance(classOf[Routes]).apigatewayRoutes, host, port)

  logger.info("http server start up at " + port)
}
