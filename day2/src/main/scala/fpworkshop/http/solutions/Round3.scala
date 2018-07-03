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

  // TODO: same target as previous exercise but
  // this time we have to compose the Option effect
  def combine(first: HttpApp, second: HttpApp): HttpApp = ???
}
