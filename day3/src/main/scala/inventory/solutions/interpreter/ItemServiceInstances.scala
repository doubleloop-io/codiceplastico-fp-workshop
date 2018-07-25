package day3.solutions.inventory.interpreter

import cats.syntax.functor._
import cats.syntax.flatMap._

import java.util.UUID

import day3.solutions.inventory._
import day3.solutions.inventory.Models._
import day3.solutions.inventory.Validation._
import day3.solutions.inventory.RandomId._
import day3.solutions.inventory.ItemRepository._
import day3.solutions.inventory.ItemService

trait ItemServiceInstances {

  implicit def itemService[F[_]: Throwing: RandomId: ItemRepository]: ItemService[F] = new ItemService[F] {

    private val TR = Throwing[F]
    import TR._

    def create(name: String, count: Int): F[Item] =
      for {
        uuid <- nextId()
        item <- liftF(Item.create(uuid, name, count))
        _    <- save(item)
      } yield item

    def deactivate(id: UUID): F[Item] =
      modify(ItemId(id), Item.deactivate)

    def checkout(id: UUID, count: Int): F[Item] =
      modifyF(ItemId(id), i => pure(i.copy(count = i.count - count)))

    def checkin(id: UUID, count: Int): F[Item] =
      modify(ItemId(id), Item.checkin(count))

    def rename(id: UUID, name: String): F[Item] =
      modify(ItemId(id), Item.rename(name))

    private def modify(id: ItemId, f: Item => ValidationResult[Item]): F[Item] =
      modifyF(id, liftF(f))

    private def modifyF(id: ItemId, f: Item => F[Item]): F[Item] =
      for {
        item0 <- load(id)
        item1 <- f(item0)
        _     <- save(item1)
      } yield item1
  }
}

object itemservice extends ItemServiceInstances
