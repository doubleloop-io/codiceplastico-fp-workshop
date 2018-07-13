package day3.solutions.inventory

trait Console[F[_]] {
  def getLine(): F[String]
  def putLine(line: String): F[Unit]
}

object Console {
  def getLine[F[_]]()(implicit C: Console[F]): F[String] =
    C.getLine()

  def putLine[F[_]](line: String)(implicit C: Console[F]): F[Unit] =
    C.putLine(line)
}
