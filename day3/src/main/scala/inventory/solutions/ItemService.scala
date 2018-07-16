package day3.solutions.inventory

import java.util.UUID
import Models._

trait ItemService[F[_]] {
  def create(id: ItemId, name: String, count: Int): F[Item]
  def deactivate(id: ItemId): F[Item]
  def checkout(id: ItemId, count: Int): F[Item]
  def checkin(id: ItemId, count: Int): F[Item]
  def rename(id: ItemId, name: String): F[Item]
}

object ItemService {

  def apply[F[_]](implicit S: ItemService[F]): ItemService[F] = S

  def create[F[_]](id: ItemId, name: String, count: Int)(implicit S: ItemService[F]): F[Item] =
    S.create(id, name, count)

  def deactivate[F[_]](id: ItemId)(implicit S: ItemService[F]): F[Item] =
    S.deactivate(id)

  def checkout[F[_]](id: ItemId, count: Int)(implicit S: ItemService[F]): F[Item] =
    S.checkout(id, count)

  def checkin[F[_]](id: ItemId, count: Int)(implicit S: ItemService[F]): F[Item] =
    S.checkin(id, count)

  def rename[F[_]](id: ItemId, name: String)(implicit S: ItemService[F]): F[Item] =
    S.rename(id, name)
}
