package day3.inventory.interpreter

import cats.effect._
import java.util.UUID

import day3.inventory.Models._
import day3.inventory.ItemRepository

trait ItemRepositoryInstances {

  implicit val itemRepositoryIO: ItemRepository[IO] = new ItemRepository[IO] {

    private var storage = Map.empty[UUID, Item]

    def load(id: UUID): IO[Item] =
      storage
        .get(id)
        .fold(IO.raiseError[Item](new Exception(s"Duplicated item: $id")))(IO.pure)

    def save(id: UUID, item: Item): IO[Unit] = {
      storage = storage + (id -> item)
      IO.unit
    }
  }
}

object itemrepository extends ItemServiceInstances
