package day2.http

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import cats.data._
import cats.implicits._

object Round8 {
  // GOAL: Write middleware once, reuse everywhere

  // TODO: Define a common abstraction for HttpApp and HttpRoutes,
  // delete the old definitions and fix the rest of the code
  // type Http[F[_]] = ???
  // type HttpApp = ???
  // type HttpRoutes = ???
  type HttpApp    = Request => Future[Response]
  type HttpRoutes = Request => OptionT[Future, Response]

  object HttpRoutes {

    // TODO: Fix me
    def of(pf: PartialFunction[Request, Future[Response]]): HttpRoutes = { req =>
      OptionT(pf.lift(req).sequence)
    }
  }

  // TODO: Fix me
  def combine(first: HttpRoutes, second: HttpRoutes): HttpRoutes =
    req => first(req) <+> second(req)

  // TODO: Fix me
  def seal(routes: HttpRoutes): HttpApp =
    routes.andThen(_.getOrElseF(Future.successful(Response(NotFound))))

  // TODO: Invoke the translator with the Response's body
  // and produce a new Response with the translated text
  // def translate[F[_]: Monad](http: Http[F]): Http[F] = ???

  def greet(theUri: Uri): HttpRoutes = HttpRoutes.of {
    case Request(POST, uri, name) if uri == theUri =>
      Future.successful(Response(OK, s"Hello, $name!"))
  }

  // TODO: uncomment after implementing the translate function
  // val hello: HttpRoutes = greet(Uri("/hello"))
  // val ciao: HttpRoutes = translate(greet(Uri("/ciao")))

  // val app: HttpApp = seal(combine(hello, ciao))

  // val appTranslateOnRoute: HttpApp = seal(translate(hello))
  // val appTranslateOnApp: HttpApp = translate(seal(hello))
}
