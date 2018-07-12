package day3.inventory

import cats._
import cats.implicits._

import RandomId._
import Console._
import ItemService._

object Examples {

  def demo[F[_]: Monad: RandomId: Console: ItemService]: F[Unit] =
    for {
      id <- nextUUID()
      _  <- putLine(id.toString)

      item0 <- create(id, "books")
      _     <- putLine(item0.toString)

      item1 <- checkin(id, 10)
      _     <- putLine(item1.toString)

      item2 <- rename(id, "pens")
      _     <- putLine(item2.toString)

      item3 <- checkout(id, 3)
      _     <- putLine(item3.toString)

      item4 <- deactivate(id)
      _     <- putLine(item4.toString)
    } yield ()
}
