package day3.solutions.inventory

import cats._
import cats.data._
import cats.implicits._
import cats.effect._
import cats.mtl.implicits._

import scala.Console.RED
import scala.Console.GREEN
import scala.Console.RESET

import interpreter.console._
import interpreter.randomid._
import interpreter.itemrepository.redis._
import interpreter.itemservice._

import Console._

object AppRedis {

  val conf = Config("localhost", 6379)

  type Result[A] = ReaderT[IO, Config, A]

  def run(): IO[Unit] = {
    val prog1 = run(Examples.demoOk[Result])
    val prog2 = run(Examples.demoBad[Result])
    val prog3 = run(Examples.demoNotFound[Result])

    val progs = Applicative[IO].tuple3(prog1, prog2, prog3)

    progs *> IO.unit
  }

  def run[A](prog: Result[A]): IO[Unit] =
    prog
      .run(conf)
      .attempt
      .flatMap(handle(_))

  def handle[A](either: Either[Throwable, A]): IO[Unit] =
    either.fold(
      e => putLine[IO](s"${RED}${e.getMessage}${RESET}"),
      a => putLine[IO](s"${GREEN}Final result: ${a}${RESET}")
    )
}
