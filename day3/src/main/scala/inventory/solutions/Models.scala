package day3.solutions.inventory

import java.util.UUID

import cats.implicits._

import Validation._

object Models {

  case class ItemId(value: UUID)
  case class Item(id: ItemId, name: String, count: Int, activated: Boolean)

  object Item {

    def create(id: UUID, name: String, count: Int): ValidationResult[Item] =
      (
        valid(ItemId(id)),
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
