package day3.solutions.inventory.interpreter

import cats.effect.Sync

import day3.solutions.inventory.Console

trait ConsoleInstances {

  implicit def console[F[_]: Sync]: Console[F] = new Console[F] {
    def getLine(): F[String]           = Sync[F].delay(io.StdIn.readLine())
    def putLine(line: String): F[Unit] = Sync[F].delay(println(line))
  }

}

object console extends ConsoleInstances
