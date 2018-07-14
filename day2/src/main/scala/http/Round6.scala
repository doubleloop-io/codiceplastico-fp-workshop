package day2.http

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import cats._
import cats.data._
import cats.implicits._

import day2.http._

object Round6 {
  // GOAL: Flip nested effects and collapse them w/ monad transformer

  type HttpApp = Request => Future[Response]
  // TODO: change return type
  // original  Option[Future[Response]]
  // flipped   Future[Option[Response]]
  // collapsed OptionT[Future[Response]]
  // and fix the rest of the code
  type HttpRoutes = Request => Option[Future[Response]]

  object HttpRoutes {

    // TODO: Fix me
    def of(pf: PartialFunction[Request, Future[Response]]): HttpRoutes = pf.lift
  }

  // TODO: Fix me
  def combine(first: HttpRoutes, second: HttpRoutes): HttpRoutes =
    req => first(req) <+> second(req)

  // TODO: Fix me
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
