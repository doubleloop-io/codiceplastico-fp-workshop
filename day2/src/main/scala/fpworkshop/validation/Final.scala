package fpworkshop.day2.validation

import scala.util.Try

object Final {

  sealed trait ValidationError
  final case object Empty extends ValidationError
  final case object TooSmall extends ValidationError
  final case object NotInteger extends ValidationError

  trait Rule[A, B] {
    def apply(value: A): Either[List[ValidationError], B]
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

  val checkNumber: Rule[String, Int] =
    value =>
      for {
        i <- checkInt(value)
        n <- checkGtZero(i)
      } yield n

}
