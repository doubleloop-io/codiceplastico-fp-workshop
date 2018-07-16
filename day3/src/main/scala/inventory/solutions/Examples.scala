package day3.solutions.inventory

import cats._
import cats.implicits._

import RandomId._
import Console._
import ItemService._
import ItemRepository._
import Models._

object Examples {

  def demoOk[F[_]: Monad: RandomId: Console: ItemService]: F[Item] =
    for {
      uuid <- nextUUID()
      id   = ItemId(uuid)
      _    <- putLine(id.toString)

      item0 <- create(id, "books", 5)
      _     <- putLine(item0.toString)

      item1 <- checkin(id, 10)
      _     <- putLine(item1.toString)

      item2 <- rename(id, "pens")
      _     <- putLine(item2.toString)

      item3 <- checkout(id, 3)
      _     <- putLine(item3.toString)

      item4 <- deactivate(id)
      _     <- putLine(item4.toString)
    } yield item4

  def demoBad[F[_]: Monad: RandomId: Console: ItemService]: F[Item] =
    for {
      uuid <- nextUUID()
      id   = ItemId(uuid)
      _    <- putLine(id.toString)

      item0 <- create(id, "@books!", -5)
      _     <- putLine(item0.toString)

      item1 <- checkin(id, 10)
      _     <- putLine(item1.toString)
    } yield item1

  def demoNotFound[F[_]: Monad: RandomId: Console: ItemService]: F[Item] =
    for {
      uuid <- nextUUID()
      id   = ItemId(uuid)
      _    <- putLine(id.toString)

      item0 <- checkin(id, 10)
      _     <- putLine(item0.toString)

    } yield item0

}
