package day3.solutions.inventory

import cats.effect._

import scala.Console.RED
import scala.Console.GREEN
import scala.Console.RESET

import interpreter.console._
import interpreter.randomid._
import interpreter.itemservice._

import Models._
import Console._

object App {

  def run(): IO[Unit] = {
    val prog1 = Examples.demoOk[Result]
    val prog2 = Examples.demoBadName[Result]

    prog1.value
      .flatMap(handle(_))
      .flatMap(_ => prog2.value.flatMap(handle(_)))
  }

  def handle(either: Either[ValidationError, Item]): IO[Unit] =
    either.fold(
      e => putLine[IO](s"${RED}${e.errorMessage}${RESET}"),
      i => putLine[IO](s"${GREEN}Final result: ${i}${RESET}")
    )
}
