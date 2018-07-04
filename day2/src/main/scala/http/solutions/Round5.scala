package day2.http.solutions

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import day2.http._

object Round5 {
  // GOAL: Introduce asynchrony

  type HttpApp = Request => Future[Response]
  type HttpRoutes = Request => Option[Future[Response]]

  object HttpRoutes {
    def of(pf: PartialFunction[Request, Future[Response]]): HttpRoutes = pf.lift
  }

  def combine(first: HttpRoutes, second: HttpRoutes): HttpRoutes = { req =>
    first(req) orElse second(req)
  }

  def seal(routes: HttpRoutes): HttpApp =
    routes.andThen(_.getOrElse(Future.successful(Response(NotFound))))

  val hello: HttpRoutes = HttpRoutes.of {
    case Request(POST, Uri("/hello"), name) =>
      Future.successful(Response(OK, s"Hello, $name!"))
  }

  val ciao: HttpRoutes = HttpRoutes.of {
    case Request(POST, Uri("/ciao"), name) =>
      Translator
        .italian(s"Hello, $name!")
        .map(Response(OK, _))
  }

  val app: HttpApp = seal(combine(hello, ciao))
}
