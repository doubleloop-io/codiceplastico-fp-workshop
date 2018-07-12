package day3.inventory

import java.util.UUID

import cats._
import cats.data._
import cats.implicits._

import Checkers._

object Models {

  case class Item(id: UUID, name: String, count: Int, activated: Boolean)

  object Item {

    def create(id: UUID, name: String, count: Int): ValidationResult[Item] =
      (
        passthru(id),
        validateName(name),
        validateCount(count),
        passthru(true)
      ).mapN(Item.apply)

    def validateName(name: String): ValidationResult[String] =
      checkNotEmpty(name, "name")
        .andThen(checkAlphanumeric(_, "name"))

    def validateCount(count: Int): ValidationResult[Int] =
      checkPositive(count, "count")
  }
}
