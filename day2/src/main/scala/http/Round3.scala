package day2.http

object Round3 {
  // GOAL: From PartialFunction to Effect

  // NOTE: The return type changed
  // from Response
  // to   Option[Response]
  type HttpApp = Request => Option[Response]

  object HttpApp {

    // NOTE: combinato to convert from PartialFunction
    def of(pf: PartialFunction[Request, Response]): HttpApp = pf.lift
  }

  // TODO: same goal as previous exercise but
  // this time we have to compose the Option effect
  def combine(first: HttpApp, second: HttpApp): HttpApp = ???

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
