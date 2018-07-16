package demos

object FpStructuresDemo {

  // Functor
  // sequential static computations
  trait Functor[F[_]] {
    // All instances of the `Functor` type-class must satisfy two laws.
    // These laws are not checked by the compiler. These laws are given as:
    //
    // * The law of identity
    //   `∀x. (id <$> x) ≅ x`
    //
    // * The law of composition
    //   `∀f g x.(f . g <$> x) ≅ (f <$> (g <$> x))`
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  // Monad
  // sequential dynamic computations
  trait Monad[F[_]] extends Applicative[F] {
    // All instances of the `Monad` type/class must satisfy one law.
    // This law is not checked by the compiler. This law is given as:
    //
    // * The law of associativity
    //   `∀f g x. g =<< (f =<< x) ≅ ((g =<<) . f) =<< x`
    def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

    // A Monad is strictly more powerful than an Applicative and Functor,
    // which means that they operations can be defined in terms of the Monad operations.
    // However, for Applicative, this definition means that the independent operations are actually evaluated sequentially.
    override def map[A, B](fa: F[A])(f: A => B): F[B] =
      flatMap(fa)(a => pure(f(a)))

    override def ap[A, B](fa: F[A])(ff: F[A ⇒ B]): F[B] =
      flatMap(fa)(a => map(ff)(f => f(a)))

  }

  // indipendent computations
  trait Applicative[F[_]] extends Functor[F] {
    // All instances of the `Applicative` type/class must satisfy three laws.
    // These laws are not checked by the compiler. These laws are given as:
    //
    // * The law of associative composition
    //   `∀a b c. ((.) <$> a <*> b <*> c) ≅ (a <*> (b <*> c))`
    //
    // * The law of identity
    //   `∀x. pure id <*> x ≅ x`
    //
    // * The law of homomorphism
    //   `∀f x. pure f <*> pure x ≅ pure (f x)`
    //
    // * The law of composition
    //   `∀u v w. pure (.) <*> u <*> v <*> w ≅ u <*> (v <*> w)`
    def pure[A](x: A): F[A]
    def ap[A, B](fa: F[A])(ff: F[A ⇒ B]): F[B]

    // derived combinator for all possible F[_] (universally quantified)
    def map2[A, B, Z](fa: F[A], fb: F[B])(f: (A, B) => Z): F[Z] =
      ap(fa)(map(fb)(b => f(_, b)))

    // An Applicative is strictly more powerful than a Functor,
    // which means that the it operations can be defined in terms of the Applicative operations.
    override def map[A, B](fa: F[A])(f: A => B): F[B] =
      ap(fa)(pure(f))
  }
}
