package day2.fpworkshop.http

object Round4 {
  // INFO: Introduce fallback route as cross-cutting combinator

  // NOTE: The central abtraction has been splitted in two:
  // - HttpRoutes that encode: maybe a route can provide a response
  // - HttpApp that encode: an application must always provide a response
  type HttpApp = Request => Response
  type HttpRoutes = Request => Option[Response]

  object HttpRoutes {
    def of(pf: PartialFunction[Request, Response]): HttpRoutes = pf.lift
  }

  val hello: HttpRoutes = HttpRoutes.of {
    case Request(POST, Uri("/hello"), name) =>
      Response(OK, s"Hello, $name!")
  }

  val ciao: HttpRoutes = HttpRoutes.of {
    case Request(POST, Uri("/ciao"), name) =>
      Response(OK, s"Ciao, $name!")
  }

  val app: HttpApp = seal(combine(hello, ciao))

  def combine(first: HttpRoutes, second: HttpRoutes): HttpRoutes = { req =>
    first(req) orElse second(req)
  }

  // TODO: Implements the combinator that attach
  // at the end of the routes chain a fallback route
  // that always match and produces a NotFound response
  def seal(routes: HttpRoutes): HttpApp = ???
}
