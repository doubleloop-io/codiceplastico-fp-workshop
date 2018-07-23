package day2.validation

import scala.util.Try

import cats._
import cats.implicits._

object Round6 {
  // GOAL: Override applicative product combinator

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

    override def product[A, B](fa: Result[A], fb: Result[B]): Result[(A, B)] =
      ???
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
      (checkNotEmpty(nameRaw), checkNumber(ageRaw)).mapN(Person.apply)
  }
}
