package day2.validation

object Round2 {

  // Build basic combinators

  sealed trait ValidationError

  type Result[A] = Either[List[ValidationError], A]

  trait Rule[A, B] {
    def apply(value: A): Result[B]
  }

  val checkGtZero: Rule[Int, Int] = ???

  // TODO: checkNotEmpty

  // TODO: checkInt

}
