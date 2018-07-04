package day2.http

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import day2.http._

object Round6 {
  // GOAL: Flip nested effects and collapse them w/ monad transformer

  object Translator {

    def italian(text: String): Future[String] = Future {
      text match {
        case "Hello, matteo!" => s"Ciao, matteo!"
      }
    }
  }

  type HttpApp = Request => Future[Response]
  // TODO: change return type
  // original  Option[Future[Response]]
  // flipped   Future[Option[Response]]
  // collapsed OptionT[Future[Response]]
  // and fix the rest of the code
  type HttpRoutes = Request => Option[Future[Response]]

  object HttpRoutes {
    def of(pf: PartialFunction[Request, Future[Response]]): HttpRoutes = pf.lift
  }

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

  def combine(first: HttpRoutes, second: HttpRoutes): HttpRoutes = { req =>
    first(req) orElse second(req)
  }

  def seal(routes: HttpRoutes): HttpApp =
    routes.andThen(_.getOrElse(Future.successful(Response(NotFound))))
}
