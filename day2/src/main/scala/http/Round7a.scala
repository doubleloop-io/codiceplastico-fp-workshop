package day2.http

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import day2.http._

object Round7a {
  // GOAL: Introduce app middleware

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

  val appTranslateOnRoute: HttpApp = seal(translateR(hello))
  val appTranslateOnApp: HttpApp = translateA(seal(hello))

  def combine(first: HttpRoutes, second: HttpRoutes): HttpRoutes = { req =>
    first(req) orElse second(req)
  }

  def seal(routes: HttpRoutes): HttpApp =
    routes.andThen(_.getOrElse(Future.successful(Response(NotFound))))

  def translateR(route: HttpRoutes): HttpRoutes =
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

  // TODO: invoke the Translator after the app
  def translateA(route: HttpApp): HttpApp = ???
}
