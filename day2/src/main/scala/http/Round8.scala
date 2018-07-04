package day2.http

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import cats._
import cats.data._
import cats.implicits._

import day2.http._

object Round8 {
  // GOAL: Write middleware once, reuse everywhere

  object Translator {

    def italian[F[_]: Monad](text: String): F[String] =
      Monad[F].pure(text match {
        case "Hello, matteo!" => s"Ciao, matteo!"
      })
  }

  // TODO: Define a common abstraction for HttpApp and HttpRoutes
  // and delete the old definitions
  // type Http[F[_]] = ???
  // type HttpApp = ???
  // type HttpRoutes = ???
  type HttpApp = Request => Future[Response]
  type HttpRoutes = Request => OptionT[Future, Response]

  object HttpRoutes {

    // TODO: Fix me
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

  // TODO: uncomment after implementing the translate function
  // val appTranslateOnRoute: HttpApp = seal(translate(hello))
  // val appTranslateOnApp: HttpApp = translate(seal(hello))

  // TODO: Fix me
  def combine(first: HttpRoutes, second: HttpRoutes): HttpRoutes = { req =>
    first(req) orElse second(req)
  }

  // TODO: Fix me
  def seal(routes: HttpRoutes): HttpApp =
    routes.andThen(_.getOrElseF(Future.successful(Response(NotFound))))

  // def translate[F[_]: Monad](http: Http[F]): Http[F] = ???
}
