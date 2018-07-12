package day3.inventory.interpreter

import cats._
import cats.implicits._
import cats.effect.IO
import java.util.UUID

import day3.inventory.Models._
import day3.inventory.ItemRepository
import day3.inventory.ItemService

trait ItemServiceInstances extends ItemRepositoryInstances {

  implicit def itemServiceIO(implicit repo: ItemRepository[IO]): ItemService[IO] = new ItemService[IO] {

    def create(id: UUID, name: String): IO[Item] = {
      val item = Item(id, name, 0, true)
      repo.save(id, item) *> IO.pure(item)
    }

    def deactivate(id: UUID): IO[Item] =
      modify(id, i => i.copy(activated = false))

    def checkout(id: UUID, count: Int): IO[Item] =
      modify(id, i => i.copy(count = i.count - count))

    def checkin(id: UUID, count: Int): IO[Item] =
      modify(id, i => i.copy(count = i.count + count))

    def rename(id: UUID, name: String): IO[Item] =
      modify(id, i => i.copy(name = name))

    private def modify(id: UUID, f: Item => Item): IO[Item] =
      for {
        item    <- repo.load(id)
        updated = f(item)
        _       <- repo.save(id, updated)
      } yield updated
  }
}

object itemservice extends ItemServiceInstances
