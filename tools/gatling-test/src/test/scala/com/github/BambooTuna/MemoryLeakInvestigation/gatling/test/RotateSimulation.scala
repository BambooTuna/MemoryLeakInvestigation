package com.github.BambooTuna.MemoryLeakInvestigation.gatling.test

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class RotateSimulation extends Simulation {

  val request = 100 //   /s
  val set     = 50  //   セット回数

  val httpConf = http
    .baseUrl("http://localhost")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scn = scenario("ping")
    .exec(
      http("rotate")
        .get("/rotate")
    )
    .pause(100.milliseconds)

  setUp(
    scn.inject(
      rampUsers(request * set).during(set.seconds)
    )
  ).protocols(httpConf)

}
