package day2.fpworkshop.http

import minitest._

import day2.fpworkshop.http.solutions.Round1._

object Round1Tests extends SimpleTestSuite {

  test("match the route") {
    val req = Request(POST, Uri("/hello"), "matteo")
    val res = app(req)
    assertEquals(Response(OK, "Hello, matteo!"), res)
  }

  test("fallback route") {
    val req = Request(POST, Uri("/not-hello"), "matteo")
    val res = app(req)
    assertEquals(Response(NotFound), res)
  }

}
