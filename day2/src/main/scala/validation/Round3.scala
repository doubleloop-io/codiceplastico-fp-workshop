package fpworkshop.day2.validation

import scala.util.Try

object Round3 {
  // build derived combinators

  sealed trait ValidationError
  final case object Empty extends ValidationError
  final case object TooSmall extends ValidationError
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

  // TODO: the string must be a positive integer
  val checkNumber: Rule[String, Int] = ???

  case class Person(name: String, age: Int)

  // TODO: not empty name and positive age
  val checkPerson: Rule[(String, String), Person] = ???

}
