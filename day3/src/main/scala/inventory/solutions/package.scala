package day3.solutions

import cats._

package object inventory {

  case class Config(redisHost: String, redisPort: Int)

  type Throwing[F[_]] = MonadError[F, Throwable]

  object Throwing {
    def apply[F[_]](implicit T: Throwing[F]): Throwing[F] = T
  }
}
