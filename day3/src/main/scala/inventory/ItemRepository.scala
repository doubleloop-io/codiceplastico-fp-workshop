package day3.inventory

import java.util.UUID
import Models._

trait ItemRepository[F[_]] {
  def load(id: UUID): F[Item]
  def save(id: UUID, item: Item): F[Unit]
}

object ItemRepository {
  def load[F[_]](id: UUID)(implicit R: ItemRepository[F]): F[Item] =
    R.load(id)

  def save[F[_]](id: UUID, item: Item)(implicit R: ItemRepository[F]): F[Unit] =
    R.save(id, item)
}
