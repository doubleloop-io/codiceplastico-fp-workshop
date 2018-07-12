package day3.inventory.interpreter

import cats.effect.IO
import java.util.UUID

import day3.inventory.RandomId

trait RandomInstances {

  implicit val randomIdIO: RandomId[IO] = new RandomId[IO] {
    def nextUUID(): IO[UUID] = IO(UUID.randomUUID())
  }

}

object randomid extends RandomInstances
