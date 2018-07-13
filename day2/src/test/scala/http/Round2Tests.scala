package day2.http

import minitest._

import day2.http.solutions.Round2._

object Round2Tests extends SimpleTestSuite {

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

}
