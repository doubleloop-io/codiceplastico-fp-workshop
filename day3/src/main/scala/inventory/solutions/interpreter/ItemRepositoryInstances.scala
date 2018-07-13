package day3.solutions.inventory.interpreter

import cats.effect.Sync
import java.util.UUID

import day3.solutions.inventory.Models._
import day3.solutions.inventory.ItemRepository
import day3.solutions.inventory.ItemRepository.ItemNotFoundException

trait ItemRepositoryInstances {

  implicit def itemRepository[F[_]: Sync]: ItemRepository[F] = new ItemRepository[F] {

    private val S = Sync[F]
    import S._

    private var storage = Map.empty[UUID, Item]

    def load(id: UUID): F[Item] =
      storage
        .get(id)
        .fold(raiseError[Item](new ItemNotFoundException(id)))(pure)

    def save(id: UUID, item: Item): F[Item] = delay {
      storage = storage + (id -> item)
      item
    }
  }
}

object itemrepository extends ItemRepositoryInstances
