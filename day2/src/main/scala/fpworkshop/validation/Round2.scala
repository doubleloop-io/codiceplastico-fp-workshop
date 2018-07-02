package fpworkshop

object Round2 {

  trait ValidationError

  trait Rule[A, B] {
    def check(value: A): Either[List[ValidationError], B]
  }

}
