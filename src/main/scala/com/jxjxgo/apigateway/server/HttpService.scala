package com.jxjxgo.apigateway.server

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
import com.google.inject.{AbstractModule, Binding, Guice, Injector, Key, TypeLiteral}
import com.jxjxgo.account.rpc.domain.AccountEndpoint
import com.jxjxgo.apigateway.action._
import com.jxjxgo.apigateway.base.ApiConfigContext
import com.jxjxgo.apigateway.service
import com.jxjxgo.apigateway.service.{ActionExecuteService, _}
import com.jxjxgo.common.exception.ServiceException
import com.jxjxgo.common.helper.ConfigHelper
import com.jxjxgo.edcenter.rpc.domain.EdServiceEndpoint
import com.jxjxgo.i18n.rpc.domain.I18NEndpoint
import com.jxjxgo.memberber.rpc.domain.MemberEndpoint
import com.jxjxgo.sms.rpc.domain.SmsServiceEndpoint
import com.jxjxgo.sso.rpc.domain.SSOServiceEndpoint
import com.twitter.finagle.Thrift
import com.twitter.util.Future
import com.typesafe.config.{Config, ConfigFactory}
import org.aopalliance.intercept.{MethodInterceptor, MethodInvocation}
import org.slf4j.{Logger, LoggerFactory}

object HttpService {
  private[this] val logger: Logger = LoggerFactory.getLogger(getClass)
  private[this] var injector: Injector = _
  private[this] var actionBeanClassList: util.ArrayList[Class[_]] = _

  def main(args: Array[String]): Unit = {

    val conf = ConfigFactory.load()

    val apiMethodInterceptor = new MethodInterceptor {
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
        }
        logger.info("Call method " + name + " cost " + (System.currentTimeMillis() - millis))
        proceed
      }
    }

    injector = Guice.createInjector(new AbstractModule() {
      override def configure() {
        val map: util.HashMap[String, String] = ConfigHelper.configMap
        Names.bindProperties(binder(), map)

        val config: Config = ConfigFactory.load()
        bind(classOf[MemberService]).to(classOf[MemberServiceImpl]).asEagerSingleton()
        bind(classOf[I18NService]).to(classOf[I18NServiceImpl]).asEagerSingleton()
        bind(classOf[SmsService]).to(classOf[SmsServiceImpl]).asEagerSingleton()
        bind(classOf[SessionService]).to(classOf[SessionServiceImpl]).asEagerSingleton()
        bind(classOf[ActionExecuteService]).to(classOf[ActionExecuteServiceImpl]).asEagerSingleton()
        bind(new TypeLiteral[MemberEndpoint[Future]]() {}).toInstance(Thrift.client.newIface[MemberEndpoint[Future]](config.getString("member.thrift.host.port")))
        bind(new TypeLiteral[EdServiceEndpoint[Future]]() {}).toInstance(Thrift.client.newIface[EdServiceEndpoint[Future]](config.getString("edcenter.thrift.host.port")))
        bind(new TypeLiteral[AccountEndpoint[Future]]() {}).toInstance(Thrift.client.newIface[AccountEndpoint[Future]](config.getString("account.thrift.host.port")))
        bind(new TypeLiteral[SSOServiceEndpoint[Future]]() {}).toInstance(Thrift.client.newIface[SSOServiceEndpoint[Future]](config.getString("sso.thrift.host.port")))
        bind(new TypeLiteral[I18NEndpoint[Future]]() {}).toInstance(Thrift.client.newIface[I18NEndpoint[Future]](config.getString("i18n.thrift.host.port")))
        bind(new TypeLiteral[SmsServiceEndpoint[Future]]() {}).toInstance(Thrift.client.newIface[SmsServiceEndpoint[Future]](config.getString("sms.thrift.host.port")))
        bind(classOf[SmsAction]).to(classOf[SmsActionImpl]).asEagerSingleton()
        bind(classOf[SSOAction]).to(classOf[SSOActionImpl]).asEagerSingleton()
        bind(classOf[I18NAction]).to(classOf[I18NActionImpl]).asEagerSingleton()
        bind(classOf[AccountService]).to(classOf[AccountServiceImpl]).asEagerSingleton()
        //      bindInterceptor(Matchers.any(), Matchers.annotatedWith(classOf[ApiMapping]), apiMethodInterceptor)
        bindInterceptor(Matchers.any(), Matchers.any(), apiMethodInterceptor)
      }
    })

    val bindings: util.Map[Key[_], Binding[_]] = injector.getBindings

    val iterator: util.Iterator[Entry[Key[_], Binding[_]]] = bindings.entrySet().iterator()

    actionBeanClassList = new util.ArrayList[Class[_]]()
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

    ApiConfigContext.initActionMap

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

  def getInjector: Injector = injector
  def getActionBeanClassList: util.ArrayList[Class[_]] = actionBeanClassList

}
