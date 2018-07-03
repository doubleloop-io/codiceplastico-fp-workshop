package day2.fpworkshop.http.solutions

import day2.fpworkshop.http._

object Round3 {
  // INFO: From PartialFunction to Effect

  type HttpApp = Request => Option[Response]

  object HttpApp {
    def of(pf: PartialFunction[Request, Response]): HttpApp = pf.lift
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

  def combine(first: HttpApp, second: HttpApp): HttpApp = { req =>
    first(req) orElse second(req)
  }
}
