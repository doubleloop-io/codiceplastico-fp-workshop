package day2.http.solutions

import day2.http._

object Round3 {
  // GOAL: From PartialFunction to Effect

  type HttpApp = Request => Option[Response]

  object HttpApp {
    def of(pf: PartialFunction[Request, Response]): HttpApp = pf.lift
  }

  def combine(first: HttpApp, second: HttpApp): HttpApp = { req =>
    first(req).orElse(second(req))
  }

  val hello: HttpApp = HttpApp.of {
    case Request(POST, Uri("/hello"), name) =>
      Response(OK, s"Hello, $name!")
  }

  val ciao: HttpApp = HttpApp.of {
    case Request(POST, Uri("/ciao"), name) =>
      Response(OK, s"Ciao, $name!")
  }

  val app: HttpApp = combine(hello, ciao)
}
