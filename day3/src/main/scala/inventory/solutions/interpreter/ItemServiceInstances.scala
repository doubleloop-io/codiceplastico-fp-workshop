package day3.solutions.inventory.interpreter

import cats.implicits._

import java.util.UUID

import day3.solutions.inventory._
import day3.solutions.inventory.Models._
import day3.solutions.inventory.RandomId
import day3.solutions.inventory.ItemRepository
import day3.solutions.inventory.ItemService

trait ItemServiceInstances {

  implicit def itemService[F[_]: Throwing: RandomId: ItemRepository]: ItemService[F] = new ItemService[F] {

    private val RID = RandomId[F]
    import RID._

    private val IR = ItemRepository[F]
    import IR._

    def create(name: String, count: Int): F[Item] =
      for {
        uuid <- nextId()
        item <- Item.createF(uuid, name, count)(Throwing[F])
        _    <- save(item)
      } yield item

    def deactivate(id: UUID): F[Item] =
      modify(ItemId(id), i => i.copy(activated = false))

    def checkout(id: UUID, count: Int): F[Item] =
      modify(ItemId(id), i => i.copy(count = i.count - count))

    def checkin(id: UUID, count: Int): F[Item] =
      modify(ItemId(id), i => i.copy(count = i.count + count))

    def rename(id: UUID, name: String): F[Item] =
      modify(ItemId(id), i => i.copy(name = name))

    private def modify(id: ItemId, f: Item => Item): F[Item] =
      for {
        item0 <- load(id)
        item1 = f(item0)
        _     <- save(item1)
      } yield item1
  }
}

object itemservice extends ItemServiceInstances
