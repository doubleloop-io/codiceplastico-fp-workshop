package day2.validation

object Round2 {

  // GOAL: Build basic combinators

  sealed trait ValidationError

  type Result[A] = Either[List[ValidationError], A]

  trait Rule[A, B] {
    def apply(value: A): Result[B]
  }

  // TODO: Implement the rule
  val checkGtZero: Rule[Int, Int] = ???

  // TODO: Implement the rule checkNotEmpty

  // TODO: Implement the rule checkInt
}
