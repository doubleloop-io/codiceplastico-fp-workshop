package fpworkshop

object Round2 {

  // Build basic combinators

  sealed trait ValidationError

  trait Rule[A, B] {
    def check(value: A): Either[List[ValidationError], B]
  }

  val checkGtZero: Rule[Int, Int] = ??? 

  // TODO: checkNotEmpty
  
  // TODO: checkInt

}
