package day3.solutions.inventory.interpreter

import cats._
import cats.effect._

import java.util.UUID

import day3.solutions.inventory._
import day3.solutions.inventory.Models._
import day3.solutions.inventory.ItemRepository
import day3.solutions.inventory.ItemService

trait ItemServiceInstances {

  implicit def itemService[F[_]: Throwing](implicit repo: ItemRepository[F]): ItemService[F] = new ItemService[F] {

    val TH = Throwing[F]
    import TH._

    def create(id: ItemId, name: String, count: Int): F[Item] =
      flatMap(Item.createF(id, name, count))(item => repo.save(id, item))

    def deactivate(id: ItemId): F[Item] =
      modify(id, i => i.copy(activated = false))

    def checkout(id: ItemId, count: Int): F[Item] =
      modify(id, i => i.copy(count = i.count - count))

    def checkin(id: ItemId, count: Int): F[Item] =
      modify(id, i => i.copy(count = i.count + count))

    def rename(id: ItemId, name: String): F[Item] =
      modify(id, i => i.copy(name = name))

    private def modify(id: ItemId, f: Item => Item): F[Item] =
      flatMap(repo.load(id))(item => repo.save(id, f(item)))
  }
}

object itemservice extends ItemServiceInstances
