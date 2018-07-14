package day2.http

import cats._
import cats.data._
import cats.implicits._

object Round4 {
  // GOAL: Re-introduce fallback route as cross-cutting combinator

  // NOTE: The central abtraction has been splitted in two:
  // - HttpRoutes that encode: maybe a route can provide a response
  // - HttpApp that encode: an application must always provide a response
  type HttpApp    = Request => Response
  type HttpRoutes = Request => Option[Response]

  object HttpRoutes {
    def of(pf: PartialFunction[Request, Response]): HttpRoutes = pf.lift
  }

  def combine(first: HttpRoutes, second: HttpRoutes): HttpRoutes =
    req => first(req) <+> second(req)

  // TODO: Implements the combinator that attach
  // at the end of the routes a fallback route
  // that always match and produces a NotFound response
  def seal(routes: HttpRoutes): HttpApp = ???

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
