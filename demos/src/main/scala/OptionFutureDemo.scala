package demos

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import cats._

object OptionFutureDemo {

  case class OptionFuture[A](value: Option[Future[A]])

  implicit val optionFutureMonad: _root_.cats.Monad[_root_.demos.OptionFutureDemo.OptionFuture] with _root_.cats.StackSafeMonad[_root_.demos.OptionFutureDemo.OptionFuture] = new Monad[OptionFuture] with StackSafeMonad[OptionFuture] {

    def pure[A](a: A): OptionFuture[A] = OptionFuture(Some(Future(a)))

    def flatMap[A, B](fa: OptionFuture[A])(
        f: A => OptionFuture[B]
    ): OptionFuture[B] =
      OptionFuture(fa.value match {
        case Some(future) => ??? // Can't implement w/out blocking threads
        case None         => None
      })
  }
}
