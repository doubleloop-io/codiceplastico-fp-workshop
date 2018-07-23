package demos

object TypeLambdaDemo {

  // Notice Point has one unbound type parameter.
  trait Point[M[_]] {
    def point[A](a: A): M[A]
  }

  // Option has one type parameter so we can just provide it as the parameter to the Monad trait.
  implicit val optionPoint: _root_.demos.TypeLambdaDemo.Point[_root_.scala.Option] = new Point[Option] {
    def point[A](a: A): Option[A] =
      Some(a)
  }

  // Either[L, R] has two type parameters.
  // Since Monad has only one type parameter, we need to curry one of the type parameters.
  // The beast :-)
  // ({ type K[+R] = Either[L, R] })#K
  // it's an inline definition and export of a type alias K with one type parameter
  // Then there is only one unbound typeparameter, R.
  // Since there is a right-bias in the Scala community, when it comes to monads for Either, we curry bind the left type param, L.
  implicit def eitherPoint[L]: _root_.demos.TypeLambdaDemo.Point[({ type T[R] = _root_.scala.util.Either[L, point] })#T] = new Point[({ type K[+R] = Either[L, R] })#K] {
    def point[R](r: R): Either[L, R] =
      Right(r)
  }

  // Other examples

  case class Foo[F[_]](f: F[_])
  case class BiParams[X, Y]()

  type FooOfBi1[A] = BiParams[String, A]
  type FooOfBi2[A] = BiParams[A, String]
  type FooOfBi3[A] = Foo[({ type Name[B] = BiParams[A, B] })#Name]

  class BiParamsC[X] {
    type K[Y] = BiParams[X, Y]
  }
  type FooOfBi4[A] = Foo[BiParamsC[A]#K]

// With kind-projector compiler plugin.
// kind-projector aims to make this syntax nicer via macros.
// See https://github.com/non/kind-projector for more information.
  type FooOfBi5[A] = Foo[BiParams[A, ?]]
}
