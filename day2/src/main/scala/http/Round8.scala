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

  type HttpApp = Request => Future[Response]
  type HttpRoutes = Request => Option[Future[Response]]

  // TODO: Define a common abstraction for HttpApp and HttpRoutes
  // and delete the previous definitions
  // type Http[F[_]] = ???
  // type HttpApp = ???
  // type HttpRoutes = ???

  // TODO: Fix compilation errors
  object HttpRoutes {
    def of(pf: PartialFunction[Request, Future[Response]]): HttpRoutes = pf.lift
  }

  def greet(theUri: Uri): HttpRoutes = HttpRoutes.of {
    case Request(POST, uri, name) if uri == theUri =>
      Future.successful(Response(OK, s"Hello, $name!"))
  }

  val hello: HttpRoutes = greet(Uri("/hello"))

  // TODO: Fix compilation errors
  def combine(first: HttpRoutes, second: HttpRoutes): HttpRoutes = { req =>
    first(req) orElse second(req)
  }

  // TODO: Fix compilation errors
  def seal(routes: HttpRoutes): HttpApp =
    routes.andThen(_.getOrElse(Future.successful(Response(NotFound))))

  // TODO: Implement
  // def translate[F[_]: Monad](http: Http[F]): Http[F] = ???

  // TODO: at the end uncomments the following code
  // val appTranslateOnRoute: HttpApp = seal(translate(hello))
  // val appTranslateOnApp: HttpApp = translate(seal(hello))

}
