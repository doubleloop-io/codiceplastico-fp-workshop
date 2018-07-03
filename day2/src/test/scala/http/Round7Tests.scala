package day2.http

import minitest._

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import day2.http.solutions.Round7._

object Round7Tests extends SimpleTestSuite {

  testAsync("translate on route") {
    val req = Request(POST, Uri("/hello"), "matteo")
    for (res <- appTranslateOnRoute(req))
      yield assertEquals(res, Response(OK, "Ciao, matteo!"))
  }

  testAsync("translate on app") {
    val req = Request(POST, Uri("/hello"), "matteo")
    for (res <- appTranslateOnApp(req))
      yield assertEquals(res, Response(OK, "Ciao, matteo!"))
  }

}
