package day3.inventory.interpreter

import cats._
import cats.implicits._
import cats.effect.Sync
import java.util.UUID

import day3.inventory.Models._
import day3.inventory.ItemRepository
import day3.inventory.ItemService

trait ItemServiceInstances extends ItemRepositoryInstances {

  implicit def itemService[F[_]: Sync](implicit repo: ItemRepository[F]): ItemService[F] = new ItemService[F] {

    def create(id: UUID, name: String, count: Int): F[Item] = {
      val item = Item(id, name, 0, true)
      repo.save(id, item) *> Sync[F].pure(item)
    }

    def deactivate(id: UUID): F[Item] =
      modify(id, i => i.copy(activated = false))

    def checkout(id: UUID, count: Int): F[Item] =
      modify(id, i => i.copy(count = i.count - count))

    def checkin(id: UUID, count: Int): F[Item] =
      modify(id, i => i.copy(count = i.count + count))

    def rename(id: UUID, name: String): F[Item] =
      modify(id, i => i.copy(name = name))

    private def modify(id: UUID, f: Item => Item): F[Item] =
      for {
        item    <- repo.load(id)
        updated = f(item)
        _       <- repo.save(id, updated)
      } yield updated
  }
}

object itemservice extends ItemServiceInstances
