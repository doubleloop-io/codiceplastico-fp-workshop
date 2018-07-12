package day3.inventory.interpreter

import cats.effect.IO

import day3.inventory.Console

trait ConsoleInstances {

  implicit val consoleIO: Console[IO] = new Console[IO] {
    def getLine(): IO[String]           = IO(io.StdIn.readLine())
    def putLine(line: String): IO[Unit] = IO(println(line))
  }

}

object console extends ConsoleInstances
