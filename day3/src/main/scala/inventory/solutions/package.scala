package day3.solutions

import cats._

package object inventory {

  lazy val enter = System.getProperty("line.separator")

  type Throwing[F[_]] = MonadError[F, Throwable]

  object Throwing {
    def apply[F[_]](implicit T: Throwing[F]): Throwing[F] = T
  }
}
