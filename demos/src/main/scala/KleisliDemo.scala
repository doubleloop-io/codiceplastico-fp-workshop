package demos

import cats._
import cats.data._
import cats.implicits._

object KleisliDemo {

  def classicFunctionComposition() {

    val parse: String => Int =
      s => s.toInt

    val reciprocal: Int => Double =
      i => 1.0 / i

    val program: String => Double =
      parse.andThen(reciprocal)

  }

  def kleisliMonadicFunctionComposition() {

    val parseK: Kleisli[Option, String, Int] =
      Kleisli(s => if (s.matches("-?[0-9]+")) Some(s.toInt) else None)

    val reciprocalK: Kleisli[Option, Int, Double] =
      Kleisli(i => if (i != 0) Some(1.0 / i) else None)

    val program: Kleisli[Option, String, Double] =
      parseK.andThen(reciprocalK)

  }
}
