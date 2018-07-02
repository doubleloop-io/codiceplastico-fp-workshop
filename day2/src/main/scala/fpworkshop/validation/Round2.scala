package fpworkshop.day2.validation

object Round2 {

  // Build basic combinators

  sealed trait ValidationError

  trait Rule[A, B] {
    def apply(value: A): Either[List[ValidationError], B]
  }

  val checkGtZero: Rule[Int, Int] = ???

  // TODO: checkNotEmpty

  // TODO: checkInt

}
