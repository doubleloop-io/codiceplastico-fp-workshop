package day2.http.solutions

import day2.http._

object Round4 {
  // GOAL: Introduce fallback route as cross-cutting combinator

  type HttpApp    = Request => Response
  type HttpRoutes = Request => Option[Response]

  object HttpRoutes {
    def of(pf: PartialFunction[Request, Response]): HttpRoutes = pf.lift
  }

  def combine(first: HttpRoutes, second: HttpRoutes): HttpRoutes = { req =>
    first(req) orElse second(req)
  }

  def seal(routes: HttpRoutes): HttpApp =
    routes.andThen(_.getOrElse(Response(NotFound)))

  val hello: HttpRoutes = HttpRoutes.of {
    case Request(POST, Uri("/hello"), name) =>
      Response(OK, s"Hello, $name!")
  }

  val ciao: HttpRoutes = HttpRoutes.of {
    case Request(POST, Uri("/ciao"), name) =>
      Response(OK, s"Ciao, $name!")
  }

  val app: HttpApp = seal(combine(hello, ciao))
}
