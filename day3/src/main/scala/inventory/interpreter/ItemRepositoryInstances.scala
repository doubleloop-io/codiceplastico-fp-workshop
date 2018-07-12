package day3.inventory.interpreter

import cats.effect.Sync
import java.util.UUID

import day3.inventory.Models._
import day3.inventory.ItemRepository

trait ItemRepositoryInstances {

  implicit def itemRepository[F[_]: Sync]: ItemRepository[F] = new ItemRepository[F] {

    private var storage = Map.empty[UUID, Item]

    def load(id: UUID): F[Item] =
      storage
        .get(id)
        .fold(Sync[F].raiseError[Item](new Exception(s"Duplicated item: $id")))(Sync[F].pure)

    def save(id: UUID, item: Item): F[Unit] = {
      storage = storage + (id -> item)
      Sync[F].unit
    }
  }
}

object itemrepository extends ItemServiceInstances
