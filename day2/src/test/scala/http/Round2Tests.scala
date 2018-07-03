package day2.fpworkshop.http

import minitest._

import day2.fpworkshop.http.solutions.Round2._

object Round2Tests extends SimpleTestSuite {

  test("match the /hello route") {
    val req = Request(POST, Uri("/hello"), "matteo")
    val res = app(req)
    assertEquals(Response(OK, "Hello, matteo!"), res)
  }

  test("match the /ciao route") {
    val req = Request(POST, Uri("/ciao"), "matteo")
    val res = app(req)
    assertEquals(Response(OK, "Ciao, matteo!"), res)
  }

}
