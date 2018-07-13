package day3.inventory.interpreter

import cats._
import cats.effect._

import java.util.UUID

import day3.inventory._
import day3.inventory.Models._
import day3.inventory.ItemRepository
import day3.inventory.ItemService

trait ItemServiceInstances extends ItemRepositoryInstances {

  implicit def itemService[F[_]](
      implicit
      repo: ItemRepository[F],
      ME: MonadError[F, ValidationError],
      S: Sync[F]
  ): ItemService[F] = new ItemService[F] {

    def create(id: UUID, name: String, count: Int): F[Item] =
      S.flatMap(Item.createF(id, name, count))(item => saveX(id, item))

    private def saveX(id: UUID, item: Item): F[Item] =
      S.map(repo.save(id, item))(_ => item)

    def deactivate(id: UUID): F[Item] =
      modify(id, i => i.copy(activated = false))

    def checkout(id: UUID, count: Int): F[Item] =
      modify(id, i => i.copy(count = i.count - count))

    def checkin(id: UUID, count: Int): F[Item] =
      modify(id, i => i.copy(count = i.count + count))

    def rename(id: UUID, name: String): F[Item] =
      modify(id, i => i.copy(name = name))

    private def modify(id: UUID, f: Item => Item): F[Item] =
      S.flatMap(repo.load(id)) { item =>
        S.map(repo.save(id, f(item)))(_ => item)
      }
  }
}

object itemservice extends ItemServiceInstances
