package com.lawsofnature.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.google.inject.{AbstractModule, Guice}
import com.lawsofnature.service.MemberService
import com.lawsofnature.service.impl.MemberServiceImpl

object HttpService extends App {

  private val injector = Guice.createInjector(new AbstractModule() {
    override def configure() {
      bind(classOf[MemberService]).to(classOf[MemberServiceImpl])
    }
  })

  private val apigatewayServer = injector.getInstance(classOf[Routes])

  implicit val system: ActorSystem = ActorSystem()

  implicit val materializer = ActorMaterializer()

  implicit val dispatcher = system.dispatcher

  Http().bindAndHandle(apigatewayServer.apigatewayRoutes, "localhost", 9010)
}
