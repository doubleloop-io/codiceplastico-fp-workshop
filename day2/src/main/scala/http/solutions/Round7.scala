package day2.http.solutions

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import cats._
import cats.data._
import cats.implicits._

import day2.http._

object Round7 {
  // GOAL: Introduce route and app middlewares

  object Translator {

    def italian(text: String): Future[String] = Future {
      text match {
        case "Hello, matteo!" => s"Ciao, matteo!"
      }
    }
  }

  type HttpApp = Request => Future[Response]
  type HttpRoutes = Request => OptionT[Future, Response]

  object HttpRoutes {
    def of(pf: PartialFunction[Request, Future[Response]]): HttpRoutes = {
      req =>
        OptionT(pf.lift(req).sequence)
    }
  }

  def greet(theUri: Uri): HttpRoutes = HttpRoutes.of {
    case Request(POST, uri, name) if uri == theUri =>
      Future.successful(Response(OK, s"Hello, $name!"))
  }

  val hello: HttpRoutes = greet(Uri("/hello"))
  val ciao: HttpRoutes = translateR(greet(Uri("/ciao")))

  val app: HttpApp = seal(combine(hello, ciao))
  val appTranslateOnRoute: HttpApp = seal(translateR(hello))
  val appTranslateOnApp: HttpApp = translateA(seal(hello))

  def combine(first: HttpRoutes, second: HttpRoutes): HttpRoutes = { req =>
    first(req) orElse second(req)
  }

  def seal(routes: HttpRoutes): HttpApp =
    routes.andThen(_.getOrElseF(Future.successful(Response(NotFound))))

  def translateR(route: HttpRoutes): HttpRoutes =
    route.andThen(
      _.semiflatMap(
        res =>
          Translator
            .italian(res.body)
            .map(txt => res.copy(body = txt))
      )
    )

  def translateA(route: HttpApp): HttpApp =
    route.andThen(
      _.flatMap(
        res =>
          Translator
            .italian(res.body)
            .map(txt => res.copy(body = txt))
      )
    )
}
