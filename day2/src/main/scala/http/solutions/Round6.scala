package day2.http.solutions

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import cats._
import cats.data._
import cats.implicits._

import day2.http._

object Round6 {
  // GOAL: Flip nested effects and collapse them w/ monad transformer

  type HttpApp    = Request => Future[Response]
  type HttpRoutes = Request => OptionT[Future, Response]

  object HttpRoutes {
    def of(pf: PartialFunction[Request, Future[Response]]): HttpRoutes = { req =>
      OptionT(pf.lift(req).sequence)
    }
  }

  def combine(first: HttpRoutes, second: HttpRoutes): HttpRoutes =
    req => first(req) <+> second(req)

  def seal(routes: HttpRoutes): HttpApp =
    routes.andThen(_.getOrElseF(Future.successful(Response(NotFound))))

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
