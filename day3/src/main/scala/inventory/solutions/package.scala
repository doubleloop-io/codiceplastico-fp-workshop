package day3.solutions

import cats._
import cats.data._
import cats.implicits._
import cats.effect._

package object inventory {

  lazy val enter = System.getProperty("line.separator")

  type Result[A] = EitherT[IO, ValidationError, A]

  type ValidationResult[A] = ValidatedNel[ValidationError, A]

  implicit class ValidationResultOps[A](actual: ValidationResult[A]) {

    def toMonadError[F[_]](implicit ME: MonadError[F, ValidationError]): F[A] =
      actual.fold(e => ME.raiseError(ErrorList(e.toList: _*)), v => ME.pure(v))
  }

  sealed trait ValidationError {
    def errorMessage: String
  }

  case class ErrorList(errors: ValidationError*) extends ValidationError {
    def errorMessage: String = errors.mkString(enter)
  }

  case class EmptyString(fieldName: String) extends ValidationError {
    def errorMessage: String = s"The $fieldName cannot be empty."
  }
  case class InvalidCharsString(fieldName: String, value: String) extends ValidationError {
    def errorMessage: String = s"The $fieldName cannot contain special characters: $value."
  }
  case class NegativeNumber(fieldName: String, value: Int) extends ValidationError {
    def errorMessage: String = s"The $fieldName cannot be negative: $value."
  }

  object Checkers {

    def passthru[A](value: A): ValidationResult[A] =
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
