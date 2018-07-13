package day3.solutions.inventory.interpreter

import cats.effect.Sync
import java.util.UUID

import day3.solutions.inventory.RandomId

trait RandomInstances {

  implicit def randomId[F[_]: Sync]: RandomId[F] = new RandomId[F] {

    private val S = Sync[F]
    import S._

    def nextUUID(): F[UUID] = delay(UUID.randomUUID())
  }

}

object randomid extends RandomInstances
