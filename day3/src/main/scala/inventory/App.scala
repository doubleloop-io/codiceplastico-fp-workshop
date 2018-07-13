package day3.inventory

import cats.effect._

import interpreter.console._
import interpreter.randomid._
import interpreter.itemservice._

object App {

  def run(): IO[Unit] =
    Examples
      .demoOk[Result]
      .value
      .flatMap(x => IO.unit)
}
