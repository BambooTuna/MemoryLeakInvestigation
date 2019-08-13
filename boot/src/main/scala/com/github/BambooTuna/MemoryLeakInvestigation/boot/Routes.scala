package com.github.BambooTuna.MemoryLeakInvestigation.boot

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{ Keep, Sink, Source }
import org.slf4j.LoggerFactory

import scala.util.{ Failure, Success }

object Routes {

  val logger = LoggerFactory.getLogger(getClass)

  def root = {
    pathSingleSlash {
      get {
        extractUri { uri =>
          complete(uri.toString())
        }
      }
    }
  }

  val source        = Source(List(1))
  val sink          = Sink.foreach[Int](v => v)
  val runnableGraph = source.toMat(sink)(Keep.right)

  def rotateActorMateriazlier(implicit system: ActorSystem) = {
    path("rotate") {
      get {
        implicit val materializer: ActorMaterializer = ActorMaterializer()
        val result                                   = runnableGraph.run()
        onComplete(result) {
          case Success(v) =>
            materializer.shutdown()
            complete(s"Success: $v")
          case Failure(exception) =>
            materializer.shutdown()
            complete(s"error: ${exception.getMessage}")
        }
      }
    }
  }

  def createActorMateriazlier(implicit system: ActorSystem) = {
    path("create") {
      get {
        implicit val materializer: ActorMaterializer = ActorMaterializer()
        val result                                   = runnableGraph.run()
        onComplete(result) {
          case Success(v)         => complete(s"Success: $v")
          case Failure(exception) => complete(s"error: ${exception.getMessage}")
        }
      }
    }
  }

  def ping = {
    path("ping") {
      get {
        complete("pong")
      }
    }
  }

  def route(implicit system: ActorSystem, materializer: ActorMaterializer): Route =
    ping ~ rotateActorMateriazlier ~ createActorMateriazlier

}

class Test() {
  val a = 1L
}
