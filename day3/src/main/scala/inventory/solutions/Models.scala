package day3.solutions.inventory

import java.util.UUID

import cats._
import cats.implicits._

import Checkers._

object Models {

  case class Item(id: UUID, name: String, count: Int, activated: Boolean)

  object Item {

    def createF[F[_]](id: UUID, name: String, count: Int)(implicit ME: MonadError[F, Throwable]): F[Item] =
      create(id, name, count).toMonadError[F]

    def create(id: UUID, name: String, count: Int): ValidationResult[Item] =
      (
        valid(id),
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
