package day2.http

import minitest._

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import day2.http.solutions.Round5._

object Round5Tests extends SimpleTestSuite {

  testAsync("match the /hello route") {
    val req = Request(POST, Uri("/hello"), "matteo")
    for (res <- app(req))
      yield assertEquals(res, Response(OK, "Hello, matteo!"))
  }

  testAsync("match the /ciao route") {
    val req = Request(POST, Uri("/ciao"), "matteo")
    for (res <- app(req))
      yield assertEquals(res, Response(OK, "Ciao, matteo!"))
  }

  testAsync("match the /hola route") {
    val req = Request(POST, Uri("/hola"), "matteo")
    for (res <- app(req))
      yield assertEquals(res, Response(OK, "Hola, matteo!"))
  }

  testAsync("fallback route") {
    val req = Request(POST, Uri("/not-hello"), "matteo")
    for (res <- app(req))
      yield assertEquals(res, Response(NotFound))
  }

}
