package demos

import cats._
import cats.data._
import cats.implicits._

object EitherDemo {

  type Result[A] = Either[List[String], A]

  val e1: Result[Int] = Right(5)
  val e2: Result[Int] = Right(6)
  val e3: Result[Int] = Left(List("Oh "))
  val e4: Result[Int] = Left(List("no!"))

  def run() = {
    val ar = Applicative[Result]
    import ar._

    println(map2(e1, e2)(_ + _))
    println(map2(e1, e3)(_ + _))
    println(map2(e3, e4)(_ + _))
    println(map4(e1, e2, e3, e4)((a, b, c, d) => a + b + c + d))

  }
}
