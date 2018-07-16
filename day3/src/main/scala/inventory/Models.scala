package day3.inventory

import java.util.UUID

import cats._
import cats.data._
import cats.implicits._

import Validation._

object Models {

  case class ItemId(value: UUID)

  case class Item(id: ItemId, name: String, count: Int, activated: Boolean)

  object Item {}
}
