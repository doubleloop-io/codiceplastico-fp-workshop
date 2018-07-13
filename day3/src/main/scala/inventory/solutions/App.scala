package day3.solutions.inventory

import cats._
import cats.data._
import cats.implicits._
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
    val prog2 = Examples.demoBad[Result]
    val prog3 = Examples.demoNotFound[Result]

    prog1
      .flatMap(_ => prog2)
      .flatMap(_ => prog3)
      .attempt
      .flatMap(handle(_))
  }

  def handle[A](either: Either[Throwable, A]): IO[Unit] =
    either.fold(
      e => putLine[IO](s"${RED}${e.getMessage}${RESET}"),
      a => putLine[IO](s"${GREEN}Final result: ${a}${RESET}")
    )
}
