package day2.http.solutions

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import cats._
import cats.data._
import cats.implicits._

import day2.http._

object Final {

  type Http[F[_]] = Kleisli[F, Request, Response]
  type HttpApp = Http[Future]
  type HttpRoutes = Http[OptionT[Future, ?]]

  object HttpApp {
    def apply(f: Request => Future[Response]): HttpApp =
      Kleisli(f)
  }

  object HttpRoutes {
    def of(pf: PartialFunction[Request, Future[Response]]): HttpRoutes =
      Kleisli(req => OptionT(pf.lift(req).sequence))
  }

  def seal(routes: HttpRoutes): HttpApp =
    routes.mapF(_.getOrElseF(Future.successful(Response(NotFound))))

  def translate[F[_]: Monad](http: Http[F]): Http[F] =
    http.flatMap(
      res =>
        Kleisli
          .liftF(Translator.italianM[F](res.body))
          .map(ita => res.copy(body = ita))
    )
}
