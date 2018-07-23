package demos

import cats.data._
import cats.implicits._

object KleisliDemo {

  def classicFunctionComposition(): Double = {

    val parse: String => Int =
      s => s.toInt

    val reciprocal: Int => Double =
      i => 1.0 / i

    val program: String => Double =
      parse.andThen(reciprocal)

    program("100")
  }

  def kleisliMonadicFunctionComposition(): Double = {

    val parseK: Kleisli[Option, String, Int] =
      Kleisli(s => if (s.matches("-?[0-9]+")) Some(s.toInt) else None)

    val reciprocalK: Kleisli[Option, Int, Double] =
      Kleisli(i => if (i != 0) Some(1.0 / i) else None)

    val program: Kleisli[Option, String, Double] =
      parseK.andThen(reciprocalK)

    program("100").getOrElse(42)
  }
}
