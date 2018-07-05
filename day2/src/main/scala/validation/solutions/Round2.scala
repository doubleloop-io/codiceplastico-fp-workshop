package day2.validation.solutions

import scala.util.Try

object Round2 {

  // GOAL: Build basic combinators

  sealed trait ValidationError
  final case object Empty      extends ValidationError
  final case object TooSmall   extends ValidationError
  final case object NotInteger extends ValidationError

  type Result[A] = Either[List[ValidationError], A]

  trait Rule[A, B] {
    def apply(value: A): Result[B]
  }

  val checkGtZero: Rule[Int, Int] =
    value =>
      if (value > 0) Right(value)
      else Left(List(TooSmall))

  val checkNotEmpty: Rule[String, String] =
    value =>
      if (value.isEmpty) Left(List(Empty))
      else Right(value)

  val checkInt: Rule[String, Int] =
    value =>
      Try(value.toInt).fold(
        _ => Left(List(NotInteger)),
        v => Right(v)
      )
}
