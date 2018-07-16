package day3.solutions.inventory

import cats.effect.IO
import java.util.UUID

trait RandomId[F[_]] {
  def nextUUID(): F[UUID]
}

object RandomId {

  def apply[F[_]]()(implicit R: RandomId[F]): RandomId[F] = R

  def nextUUID[F[_]]()(implicit R: RandomId[F]): F[UUID] =
    R.nextUUID()
}
