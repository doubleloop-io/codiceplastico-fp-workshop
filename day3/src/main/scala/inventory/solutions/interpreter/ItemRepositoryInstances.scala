package day3.solutions.inventory.interpreter

import cats.effect.Sync
import java.util.UUID

import day3.solutions.inventory.Models._
import day3.solutions.inventory.ItemRepository

trait ItemRepositoryInstances {

  implicit def itemRepository[F[_]: Sync]: ItemRepository[F] = new ItemRepository[F] {

    private var storage = Map.empty[UUID, Item]

    def load(id: UUID): F[Item] =
      Sync[F].pure(storage(id))

    def save(id: UUID, item: Item): F[Item] = {
      storage = storage + (id -> item)
      Sync[F].pure(item)
    }
  }
}

object itemrepository extends ItemServiceInstances
