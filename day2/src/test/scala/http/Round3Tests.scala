package day2.http

import minitest._

import day2.http.solutions.Round3._

object Round3Tests extends SimpleTestSuite {

  test("match the /hello route") {
    val req = Request(POST, Uri("/hello"), "matteo")
    val res = app(req).get
    assertEquals(Response(OK, "Hello, matteo!"), res)
  }

  test("match the /ciao route") {
    val req = Request(POST, Uri("/ciao"), "matteo")
    val res = app(req).get
    assertEquals(Response(OK, "Ciao, matteo!"), res)
  }

}
