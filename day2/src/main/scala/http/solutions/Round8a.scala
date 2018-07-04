package day2.http.solutions

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import cats._
import cats.data._
import cats.implicits._

import day2.http._

object Round8a {
  // GOAL: Write middleware once, reuse everywhere

  object Translator {

    def italian[F[_]: Monad](text: String): F[String] =
      Monad[F].pure(text match {
        case "Hello, matteo!" => s"Ciao, matteo!"
      })
  }

  /*

    Guardiamo le astrazioni base

    type HttpApp = Request => Future[Response]
    type HttpRoutes = Request => Option[Future[Response]]

    Noi sappiamo che con varie tecniche possiamo mascherare il dopio effetto Option[Future[A]]
    come un unico effetto, per esempio possiamo fornire un custom type.

    case class OptionFuture[A](value: Option[Future[A]])

    type HttpApp = Request => Future[Response]
    type HttpRoutes = Request => OptionFuture[Response]

    Le astrazioni di base rappresentano entrambe una funzione che va da Request
    a qualche effetto di Respose. Per poter "vedere" le due funzioni come una sola
    abbiamo bisogno di astrarre il nostro effetto tramite un polymorphic type constructor.

    type Http[F[_]] = Request => F[Response]

    E ridefinire le astrazioni solo in termini di "chiusura" dell'effetto prodotto.

    type HttpApp = Http[Future]
    type HttpRoutes = Http[OptionFuture]

    Addesso possiamo provare a scrivere i middleware in termini di Http, solo che
    ci dobbiamo sicuramente implementare le operazioni di composizione base
    come per esempio andThen per Http, questo perchè Http non è una semplice funzione ma
    una funzione che produce un valore monadico. Fortuna vuole che esiste già il Kleisli,
    un'astrazione che:
    Kleisli is just a wrapper around the function A => F[B]
    that enables composition of functions that return a monadic value.

    final case class Kleisli[F[_], A, B](run: A => F[B])

    Dalla documentazione di cats:

    We may also have several functions which depend on some environment
    and want a nice way to compose these functions to ensure they all receive the same environment.

    Nel nostro scenario l'environment è la Request.
    Adesso possiamo riscrivere le nostre astrazioni in termini di Kleisli.

    type Http[F[_]] = Kleisli[F, Request, Response]
    type HttpApp = Http[Future]
    type HttpRoutes = Http[OptionFuture]

   */

  case class OptionFuture[A](value: Option[Future[A]])

  implicit val optionFutureMonad = new Monad[OptionFuture]
  with StackSafeMonad[OptionFuture] {

    def pure[A](a: A): OptionFuture[A] = OptionFuture(Some(Future(a)))

    def flatMap[A, B](fa: OptionFuture[A])(
        f: A => OptionFuture[B]
    ): OptionFuture[B] =
      OptionFuture(fa.value match {
        case Some(future) => ??? // Can't implement w/out blocking threads
        case None         => None
      })
  }

  type Http[F[_]] = Kleisli[F, Request, Response]
  type HttpApp = Http[Future]
  type HttpRoutes = Http[OptionFuture]

  object HttpApp {
    def apply(f: Request => Future[Response]): HttpApp =
      Kleisli(f)
  }

  object HttpRoutes {
    def of(pf: PartialFunction[Request, Future[Response]]): HttpRoutes =
      Kleisli(pf.lift.andThen(OptionFuture(_)))
  }

  def greet(theUri: Uri): HttpRoutes = HttpRoutes.of {
    case Request(POST, uri, name) if uri == theUri =>
      Future.successful(Response(OK, s"Hello, $name!"))
  }

  val hello: HttpRoutes = greet(Uri("/hello"))

  val appTranslateOnRoute: HttpApp = seal(translate(hello))
  val appTranslateOnApp: HttpApp = translate(seal(hello))

  def combine(first: HttpRoutes, second: HttpRoutes): HttpRoutes = Kleisli {
    req =>
      OptionFuture(first(req).value.orElse(second(req).value))
  }

  def seal(routes: HttpRoutes): HttpApp = Kleisli { req =>
    routes.run(req).value.getOrElse(Future.successful(Response(NotFound)))
  }

  def translate[F[_]: Monad](http: Http[F]): Http[F] =
    http.flatMap(
      res =>
        italianK[F](res.body)
          .map(ita => res.copy(body = ita))
    )

  def italianK[F[_]: Monad](text: String): Kleisli[F, Request, String] =
    Kleisli.liftF(Translator.italian[F](text))
}
