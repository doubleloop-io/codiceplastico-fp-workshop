package day2.http.solutions

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import cats._
import cats.data._
import cats.implicits._

import day2.http._

object Round8 {
  // GOAL: Write middleware once, reuse everywhere

  /*

    Guardiamo le astrazioni base

    type HttpApp = Request => Future[Response]
    type HttpRoutes = Request => OptionT[Future, Response]

    Entrambe rappresentano una funzione che va da Request a qualche effetto di Respose.
    Per poter "vedere" le due funzioni come una sola abbiamo bisogno di astrarre
    l'effetto tramite un polymorphic type constructor.

    Request => F[Response]

    E ridefinire le astrazioni solo in termini di "chiusura" dell'effetto prodotto.

    type Http[F[_]] = Request => F[Response]
    type HttpApp = Http[Future]
    type HttpRoutes = Http[OptionT[Future, ?]]

    Addesso possiamo provare a scrivere i middleware in termini di Http, solo che
    ci dobbiamo sicuramente implementare le operazioni di composizione base
    come per esempio andThen per Http, questo perchè Http non è una semplice funzione ma
    una funzione che produce un valore monadico. Fortuna vuole che esiste già il Kleisli.
    Dalla documentazione di cats:

    Kleisli is just a wrapper around the function A => F[B]
    that enables composition of functions that return a monadic value.

    final case class Kleisli[F[_], A, B](run: A => F[B])

    We may also have several functions which depend on some environment
    and want a nice way to compose these functions to ensure they all receive the same environment.

    Nel nostro scenario l'environment è la Request.
    Adesso possiamo riscrivere Http in termini di Kleisli.

    type Http[F[_]] = Kleisli[F, Request, Response]

    Le altre rimangono intatte.

    type HttpApp = Http[Future]
    type HttpRoutes = Http[OptionT[Future, ?]]

   */

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

  def combine(first: HttpRoutes, second: HttpRoutes): HttpRoutes = Kleisli { req =>
    first(req) orElse second(req)
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

  def greet(theUri: Uri): HttpRoutes = HttpRoutes.of {
    case Request(POST, uri, name) if uri == theUri =>
      Future.successful(Response(OK, s"Hello, $name!"))
  }

  val hello: HttpRoutes = greet(Uri("/hello"))
  val ciao: HttpRoutes = translate(greet(Uri("/ciao")))

  val app: HttpApp = seal(combine(hello, ciao))

  val appTranslateOnRoute: HttpApp = seal(translate(hello))
  val appTranslateOnApp: HttpApp = translate(seal(hello))
}
