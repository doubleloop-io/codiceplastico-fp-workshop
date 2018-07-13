package day3.solutions.inventory.interpreter

import cats.effect.Sync
import java.util.UUID

import day3.solutions.inventory.Models._
import day3.solutions.inventory.ItemRepository

trait ItemRepositoryInstances {

  implicit def itemRepository[F[_]: Sync]: ItemRepository[F] = new ItemRepository[F] {

    private val S = Sync[F]
    import S._

    private var storage = Map.empty[UUID, Item]

    def load(id: UUID): F[Item] =
      delay(storage(id))

    def save(id: UUID, item: Item): F[Item] = delay {
      storage = storage + (id -> item)
      item
    }
  }
}

object itemrepository extends ItemServiceInstances
