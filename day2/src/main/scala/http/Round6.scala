package day2.http

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import day2.http._

object Round6 {
  // INFO: Introduce route middleware

  object Translator {

    def italianAsync(text: String): Future[String] = Future {
      text match {
        case "Hello, matteo!" => s"Ciao, matteo!"
      }
    }
  }

  type HttpApp = Request => Future[Response]
  type HttpRoutes = Request => Option[Future[Response]]

  object HttpRoutes {
    def of(pf: PartialFunction[Request, Future[Response]]): HttpRoutes = pf.lift
  }

  def greet(theUri: Uri): HttpRoutes = HttpRoutes.of {
    case Request(POST, uri, name) if uri == theUri =>
      Future.successful(Response(OK, s"Hello, $name!"))
  }

  val hello: HttpRoutes = greet(Uri("/hello"))
  val ciao: HttpRoutes = translate(greet(Uri("/ciao")))

  val app: HttpApp = seal(combine(hello, ciao))

  def combine(first: HttpRoutes, second: HttpRoutes): HttpRoutes = { req =>
    first(req) orElse second(req)
  }

  def seal(routes: HttpRoutes): HttpApp =
    routes.andThen(_.getOrElse(Future.successful(Response(NotFound))))

  // TODO: invoke the Translator after the route and only if the route match
  def translate(route: HttpRoutes): HttpRoutes = ???
}
