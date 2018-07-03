package day2.http.solutions

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import day2.http._

object Round6 {
  // GOAL: Introduce route middleware

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

  def translate(route: HttpRoutes): HttpRoutes =
    route.andThen(
      _.map(
        _.flatMap(
          res =>
            Translator
              .italianAsync(res.body)
              .map(txt => res.copy(body = txt))
        )
      )
    )
}
