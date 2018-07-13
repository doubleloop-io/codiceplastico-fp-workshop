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

    prog1.value
      .flatMap(handleVR(_))
      .flatMap(_ => prog2.value.flatMap(handleVR(_)))
      .flatMap(_ => prog3.value.flatMap(handleVR(_)))
      .attempt
      .flatMap(handleET(_))
  }

  def handleVR[A](either: Either[ValidationError, A]): IO[Unit] =
    either.fold(
      e => putLine[IO](s"${RED}${e.errorMessage}${RESET}"),
      a => putLine[IO](s"${GREEN}Final result: ${a}${RESET}")
    )

  def handleET[A](either: Either[Throwable, A]): IO[Unit] =
    either.fold(
      e => putLine[IO](s"${RED}${e.getMessage}${RESET}"),
      a => putLine[IO](s"${GREEN}Final result: ${a}${RESET}")
    )
}
