package day2.http

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import day2.http._

object Round5 {
  // GOAL: Introduce asynchrony

  object Translator {

    def greetAsync(lang: String, name: String): Future[String] =
      Future.successful(greet(lang, name))

    def greet(lang: String, name: String): String = lang match {
      case "ES" => s"Hola, $name!"
    }
  }

  // TODO: delete this definition
  type HttpApp = Request => Response
  type HttpRoutes = Request => Option[Response]

  // TODO: Uncomment this definition and fix the rest of the code
  // type HttpApp = Request => Future[Response]
  // type HttpRoutes = Request => Option[Future[Response]]

  object HttpRoutes {
    def of(pf: PartialFunction[Request, Response]): HttpRoutes = pf.lift
  }

  val hello: HttpRoutes = HttpRoutes.of {
    case Request(POST, Uri("/hello"), name) =>
      Response(OK, s"Hello, $name!")
  }

  val ciao: HttpRoutes = HttpRoutes.of {
    case Request(POST, Uri("/ciao"), name) =>
      Response(OK, s"Ciao, $name!")
  }

  val hola: HttpRoutes = HttpRoutes.of {
    case Request(POST, Uri("/hola"), name) =>
      Response(OK, Translator.greet("ES", name))
  }

  val app: HttpApp = seal(combine(hello, combine(ciao, hola)))

  def combine(first: HttpRoutes, second: HttpRoutes): HttpRoutes = { req =>
    first(req) orElse second(req)
  }

  def seal(routes: HttpRoutes): HttpApp =
    routes.andThen(_.getOrElse(Response(NotFound)))
}
