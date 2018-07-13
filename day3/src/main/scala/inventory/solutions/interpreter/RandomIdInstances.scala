package day3.solutions.inventory.interpreter

import cats.effect.Sync
import java.util.UUID

import day3.solutions.inventory.RandomId

trait RandomInstances {

  implicit def randomId[F[_]: Sync]: RandomId[F] = new RandomId[F] {
    def nextUUID(): F[UUID] = Sync[F].delay(UUID.randomUUID())
  }

}

object randomid extends RandomInstances
