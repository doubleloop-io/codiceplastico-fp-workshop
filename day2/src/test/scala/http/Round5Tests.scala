package day2.http

import minitest._

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import day2.http.solutions.Round5._

object Round5Tests extends SimpleTestSuite {

  testAsync("asynchronous execution") {
    val future = Future(100).map(_ + 1)

    for (result <- future) yield {
      assertEquals(result, 101)
    }
  }

  testAsync("match the /hello route") {
    val req = Request(POST, Uri("/hello"), "matteo")
    for (res <- app(req))
      yield assertEquals(Response(OK, "Hello, matteo!"), res)
  }

  testAsync("match the /ciao route") {
    val req = Request(POST, Uri("/ciao"), "matteo")
    for (res <- app(req))
      yield assertEquals(Response(OK, "Ciao, matteo!"), res)
  }

  testAsync("match the /hola route") {
    val req = Request(POST, Uri("/hola"), "matteo")
    for (res <- app(req))
      yield assertEquals(Response(OK, "Hola, matteo!"), res)
  }

  testAsync("fallback route") {
    val req = Request(POST, Uri("/not-hello"), "matteo")
    for (res <- app(req))
      yield assertEquals(Response(NotFound), res)
  }

}
