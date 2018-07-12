package day3.inventory

import java.util.UUID
import Models._

trait ItemService[F[_]] {
  def create(id: UUID, name: String, count: Int): F[Item]
  def create2(id: UUID, name: String, count: Int): F[ValidationResult[Item]]
  def deactivate(id: UUID): F[Item]
  def checkout(id: UUID, count: Int): F[Item]
  def checkin(id: UUID, count: Int): F[Item]
  def rename(id: UUID, name: String): F[Item]
}

object ItemService {
  def create[F[_]](id: UUID, name: String, count: Int)(implicit S: ItemService[F]): F[Item] =
    S.create(id, name, count)

  def create2[F[_]](id: UUID, name: String, count: Int)(implicit S: ItemService[F]): F[ValidationResult[Item]] =
    S.create2(id, name, count)

  def deactivate[F[_]](id: UUID)(implicit S: ItemService[F]): F[Item] =
    S.deactivate(id)

  def checkout[F[_]](id: UUID, count: Int)(implicit S: ItemService[F]): F[Item] =
    S.checkout(id, count)

  def checkin[F[_]](id: UUID, count: Int)(implicit S: ItemService[F]): F[Item] =
    S.checkin(id, count)

  def rename[F[_]](id: UUID, name: String)(implicit S: ItemService[F]): F[Item] =
    S.rename(id, name)
}
