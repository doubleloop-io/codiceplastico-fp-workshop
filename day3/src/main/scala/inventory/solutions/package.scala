package day3.solutions

import cats._
import cats.data._
import cats.implicits._
import cats.effect._

package object inventory {

  lazy val enter = System.getProperty("line.separator")

  case class Config(redisHost: String, redisPort: Int)

  type Result[A]           = ReaderT[IO, Config, A]
  type ValidationResult[A] = ValidatedNel[ValidationError, A]

  implicit class ValidationResultOps[A](actual: ValidationResult[A]) {

    def toMonadError[F[_]](implicit ME: MonadError[F, Throwable]): F[A] =
      actual.fold(
        e => ME.raiseError(ValidationErrorException(e.toList: _*)),
        v => ME.pure(v)
      )
  }

  implicit val validationErrorShow = new Show[ValidationError] {
    def show(value: ValidationError): String = value match {
      case EmptyString(fieldName)               => s"The $fieldName cannot be empty."
      case InvalidCharsString(fieldName, value) => s"The $fieldName cannot contain special characters: $value."
      case NegativeNumber(fieldName, value)     => s"The $fieldName cannot be negative: $value."
    }
  }

  final case class ValidationErrorException(errors: ValidationError*)
      extends Exception("Error list:" + enter + errors.map("- " + _.show).mkString(enter))

  sealed trait ValidationError
  case class EmptyString(fieldName: String)                       extends ValidationError
  case class InvalidCharsString(fieldName: String, value: String) extends ValidationError
  case class NegativeNumber(fieldName: String, value: Int)        extends ValidationError

  object Checkers {

    def valid[A](value: A): ValidationResult[A] =
      value.validNel

    def checkNotEmpty(value: String, fieldName: String): ValidationResult[String] =
      Either
        .cond(value.trim.nonEmpty, value, EmptyString(fieldName))
        .toValidatedNel

    def checkAlphanumeric(value: String, fieldName: String): ValidationResult[String] =
      Either
        .cond(value.matches("^[a-zA-Z0-9\\s]+$"), value, InvalidCharsString(fieldName, value))
        .toValidatedNel

    def checkPositive(value: Int, fieldName: String): ValidationResult[Int] =
      Either
        .cond(value >= 0, value, NegativeNumber(fieldName, value))
        .toValidatedNel
  }

}
