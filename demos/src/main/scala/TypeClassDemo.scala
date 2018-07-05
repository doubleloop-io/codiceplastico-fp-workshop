package demos

object TypeClassDemo {

  // The type class patter is composed of two concepts:
  // - type class definition (abstract contract)
  // - type class instances (concrete implementations)

  // Plus two optional concepts tipically needed in Scala
  // - interfce methods (simplify instances access w/ companion object)
  // - interfce syntax (simplify instances access w/ extension class)

  // Type class
  // abstract contract of some functionality in Scala is defined as parametric trait
  // note: the future implementations must be stateless, so A it's the type of
  // the state for which we want provide the functionality
  trait Printable[A] {
    def print(a: A): String
  }

  // Type class instances
  // concrete implementations defined as implicit so the compiler can inject automatically
  implicit val stringPrintable = new Printable[String] {
    def print(s: String): String = s
  }
  implicit val intPrintable = new Printable[Int] {
    def print(i: Int): String = i.toString
  }

  // Interface methods
  // in fact this step is optional but in Scala it enables an easier use of the
  // type class pattern so it's part of the definition
  object Printable {
    def apply[A](implicit P: Printable[A]): Printable[A] = P
  }

  // Syntax
  // it's another optional step for Scala and it's needed in order
  // to access to type class functionality via methods on state.
  object PrintableSyntax {
    implicit class PrintableOps[A](value: A)(implicit P: Printable[A]) {
      def print(): String = P.print(value)
    }
  }

  // How to use

  // Define a polymorphic methods w/ constraints
  // and ask for instance via interface methods
  def prettyPrint1[A: Printable](value: A): String =
    Printable[A].print(value)

  // Define a polymorphic methods w/ implicit parameters
  def prettyPrint2[A](value: A)(implicit P: Printable[A]): String =
    P.print(value)

  // Define a polymorphic methods w/ constraints
  // and access to functionality via syntax
  import PrintableSyntax._
  def prettyPrint3[A: Printable](value: A): String =
    value.print()

  // Type classes can be extended
  trait Semigroup[A] {
    def append(l: A, r: A): A
  }
  trait Monoid[A] extends Semigroup[A] {
    def empty: A
  }

  // Type classes can be governed by laws

  // Semigroup[A]
  // Associative Law:
  // append(a, append(b, c)) == append(append(a, b), c)

  // Monoid
  // Identity laws:
  // append(a, zero) == a
  // append(zero, a) == a
}
