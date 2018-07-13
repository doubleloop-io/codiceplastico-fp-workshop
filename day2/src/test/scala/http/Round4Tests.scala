package day2.http

import minitest._

import day2.http.solutions.Round4._

object Round4Tests extends SimpleTestSuite {

  test("match the /hello route") {
    val req = Request(POST, Uri("/hello"), "matteo")
    val res = app(req)
    assertEquals(res, Response(OK, "Hello, matteo!"))
  }

  test("match the /ciao route") {
    val req = Request(POST, Uri("/ciao"), "matteo")
    val res = app(req)
    assertEquals(res, Response(OK, "Ciao, matteo!"))
  }

  test("fallback route") {
    val req = Request(POST, Uri("/not-hello"), "matteo")
    val res = app(req)
    assertEquals(res, Response(NotFound))
  }

}
