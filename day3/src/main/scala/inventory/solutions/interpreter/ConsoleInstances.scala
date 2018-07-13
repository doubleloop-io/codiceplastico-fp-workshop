package day3.solutions.inventory.interpreter

import cats.effect.Sync

import day3.solutions.inventory.Console

trait ConsoleInstances {

  implicit def console[F[_]: Sync]: Console[F] = new Console[F] {

    private val S = Sync[F]
    import S._

    def getLine(): F[String]           = delay(io.StdIn.readLine())
    def putLine(line: String): F[Unit] = delay(println(line))
  }

}

object console extends ConsoleInstances
