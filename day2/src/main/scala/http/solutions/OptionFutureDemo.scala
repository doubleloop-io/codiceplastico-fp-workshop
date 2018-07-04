package day2.http.solutions

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import cats._
import cats.data._
import cats.implicits._

object OptionFutureDemo {

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
}
