package com.lawsofnature.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.google.inject.matcher.{Matcher, Matchers}
import com.google.inject.spi.{InjectionListener, TypeEncounter, TypeListener}
import com.google.inject.{AbstractModule, Guice, TypeLiteral}
import com.lawsofnature.member.client.{MemberClientService, MemberClientServiceImpl}
import com.lawsofnature.service.MemberService
import com.lawsofnature.service.impl.MemberServiceImpl

object HttpService extends App {

//  bindListener(Matchers.subclassesOf(MyInitClass.class), new TypeListener() {
//    @Override
//    public <I> void hear(final TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
//      typeEncounter.register(new InjectionListener<I>() {
//        @Override
//        public void afterInjection(Object i) {
//          MyInitClass m = (MyInitClass) i;
//          m.init();
//        }
//      });
//      }
//      });

  val injectionListener: InjectionListener[MemberClientService] = new InjectionListener[MemberClientService]() {
    override def afterInjection(memberClientService: MemberClientService): Unit = {
      memberClientService.initClient
    }
  }

//  val typeListener: TypeListener = new TypeListener() {
//    override def hear[I](typeLiteral: TypeLiteral[I], typeEncounter: TypeEncounter[I]): Unit = {
//      typeEncounter.register(injectionListener)
//    }
//  }
//
  private val injector = Guice.createInjector(new AbstractModule() {
    override def configure() {
      bind(classOf[MemberService]).to(classOf[MemberServiceImpl])
      bind(classOf[MemberClientService]).to(classOf[MemberClientServiceImpl])
//      bindListener(Matchers.subclassesOf(classOf[MemberClientService]), typeListener)
    }
  })

  private val apigatewayServer = injector.getInstance(classOf[Routes])

  implicit val system: ActorSystem = ActorSystem()

  implicit val materializer = ActorMaterializer()

  implicit val dispatcher = system.dispatcher

  Http().bindAndHandle(apigatewayServer.apigatewayRoutes, "localhost", 9010)

  println("http server start up at 9010")

//  system.scheduler.schedule()
}
