package day3.inventory

import java.util.UUID

object Models {

  case class Item(id: UUID, name: String, count: Int, activated: Boolean)

}
