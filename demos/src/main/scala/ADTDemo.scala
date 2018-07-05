package demos

object ADTDemo {

  object ProductType {

    type Point = (Int, Int)

    case class Person(name: String, age: Int)

    case class Engine(hp: Int)
    case class Wheel(width: Int, ratio: Int, model: String, rimSize: Int)
    case class Car(engine: Engine, wheels: List[Wheel])

  }

  object SumType {

    sealed trait Currency
    object USD extends Currency
    object EUR extends Currency

    sealed trait Maybe[+A]
    final case class Some[A](v: A) extends Maybe[A]
    final case object None         extends Maybe[Nothing]

    sealed trait List[A]
    final case class Nil[A]()                        extends List[A]
    final case class Cons[A](head: A, tail: List[A]) extends List[A]

    // Option and Either are Sum types
  }
}
