package day3.inventory

import cats.effect._

import interpreter.console._
import interpreter.randomid._
import interpreter.itemservice._

object App {

  def run(): IO[Unit] = {
    val prog1 = Examples.demoOk[Result]
    val prog2 = Examples.demoBadName[Result]

    prog1
      .flatMap(_ => prog2)
      .value
      .flatMap(x => IO.unit)
  }
}
