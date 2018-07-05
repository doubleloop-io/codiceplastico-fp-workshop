package day2.http

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import day2.http._

object Round5 {
  // GOAL: Introduce asynchrony

  // TODO: Add Future effect around Resonse
  // and fix the rest of the code
  type HttpApp    = Request => Response
  type HttpRoutes = Request => Option[Response]

  object HttpRoutes {
    def of(pf: PartialFunction[Request, Response]): HttpRoutes = pf.lift
  }

  def combine(first: HttpRoutes, second: HttpRoutes): HttpRoutes = { req =>
    first(req) orElse second(req)
  }

  // TODO: Fix me
  def seal(routes: HttpRoutes): HttpApp =
    routes.andThen(_.getOrElse(Response(NotFound)))

  // TODO: Fix me
  val hello: HttpRoutes = HttpRoutes.of {
    case Request(POST, Uri("/hello"), name) =>
      Response(OK, s"Hello, $name!")
  }

  // TODO: invoke the translator with the text in English
  // and produce a Response with the translated text
  val ciao: HttpRoutes = HttpRoutes.of {
    case Request(POST, Uri("/ciao"), name) =>
      Response(OK, s"Ciao, $name!")
  }

  val app: HttpApp = seal(combine(hello, ciao))
}
