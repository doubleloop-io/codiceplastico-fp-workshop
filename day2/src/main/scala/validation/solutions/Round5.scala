package day2.validation.solutions

import scala.util.Try

import cats._
import cats.data._
import cats.implicits._

object Round5 {
  // GOAL: Override applicative map2 combinator

  type FormData = Map[String, String]
  case class Person(name: String, age: Int)

  sealed trait ValidationError
  final case object Empty      extends ValidationError
  final case object TooSmall   extends ValidationError
  final case object NotInteger extends ValidationError

  sealed trait Result[+A]
  final case class Success[+A](value: A)               extends Result[A]
  final case class Fail(errors: List[ValidationError]) extends Result[Nothing]

  trait Rule[A, B] {
    def apply(value: A): Result[B]
  }

  implicit val resultMonad = new Monad[Result] with StackSafeMonad[Result] {
    def pure[A](a: A) = Success(a)

    def flatMap[A, B](fa: Result[A])(f: A => Result[B]): Result[B] = fa match {
      case Success(v)    => f(v)
      case err @ Fail(l) => err
    }

    override def map2[A, B, Z](fa: Result[A], fb: Result[B])(
        f: (A, B) ⇒ Z
    ): Result[Z] = (fa, fb) match {
      case (Success(a), Success(b))    => Success(f(a, b))
      case (err @ Fail(_), Success(_)) => err
      case (Success(_), err @ Fail(_)) => err
      case (Fail(l1), Fail(l2))        => Fail(l1 ++ l2)
    }
  }

  val checkGtZero: Rule[Int, Int] =
    value =>
      if (value > 0) Success(value)
      else Fail(List(TooSmall))

  val checkNotEmpty: Rule[String, String] =
    value =>
      if (value.isEmpty) Fail(List(Empty))
      else Success(value)

  val checkInt: Rule[String, Int] =
    value =>
      Try(value.toInt).fold(
        _ => Fail(List(NotInteger)),
        v => Success(v)
      )

  val checkNumber: Rule[String, Int] =
    value => checkInt(value).flatMap(checkGtZero(_))

  val checkPerson: Rule[(String, String), Person] = {
    case (nameRaw, ageRaw) =>
      Applicative[Result]
        .map2(checkNotEmpty(nameRaw), checkNumber(ageRaw))(Person.apply)
  }
}
