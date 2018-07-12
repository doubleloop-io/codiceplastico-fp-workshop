package day3.inventory

import cats.effect.IO

import interpreter.console._
import interpreter.randomid._
import interpreter.itemservice._

object App {
  def run(): IO[Unit] =
    Examples.demo[IO]
}
