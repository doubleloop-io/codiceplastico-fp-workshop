package day3.solutions.inventory

import java.util.UUID

import cats._
import cats.data._
import cats.implicits._

import Validation._

object Models {

  case class ItemId(value: UUID)
  case class Item(name: String, count: Int, activated: Boolean)

  object Item {

    def createF[F[_]: Throwing](name: String, count: Int): F[Item] =
      create(name, count)
        .leftMap(ValidationErrorException.apply)
        .toThrowing[F]

    def create(name: String, count: Int): ValidationResult[Item] =
      (
        validateName(name),
        validateCount(count),
        valid(true)
      ).mapN(Item.apply)

    def validateName(name: String): ValidationResult[String] =
      checkNotEmpty(name, "name")
        .andThen(checkAlphanumeric(_, "name"))

    def validateCount(count: Int): ValidationResult[Int] =
      checkPositive(count, "count")
  }
}
