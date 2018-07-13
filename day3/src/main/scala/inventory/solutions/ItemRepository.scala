package day3.solutions.inventory

import java.util.UUID
import Models._

trait ItemRepository[F[_]] {
  def load(id: UUID): F[Item]
  def save(id: UUID, item: Item): F[Item]
}

object ItemRepository {

  final case class ItemNotFoundException(id: UUID) extends Exception(s"Can't load item: $id")

  def load[F[_]](id: UUID)(implicit R: ItemRepository[F]): F[Item] =
    R.load(id)

  def save[F[_]](id: UUID, item: Item)(implicit R: ItemRepository[F]): F[Item] =
    R.save(id, item)
}
