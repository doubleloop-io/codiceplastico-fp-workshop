package demos

object KindsDemo {

  // Proper type
  // A type is a compile-time description of a set of values.
  // examples: String, Int

  // Type constructor
  // A type which accept a proper types and return types.
  // examples: List[A], Option[A]

  // Higher Kinds Type
  // A type constructor which accept a type constructors and return types.
  // examples: Functor[F[_]], Monad[F[_]]

  // Kinds can be thought of as *the type of types*.
  //  `*` — The kind of types (the set of all types).
  //  `* => *` — The kind of type-level functions that accept 1 type (the set of all type-level functions that accept 1 type).
  //  `[*, *] => *` — The kind of type-level functions that accept 2 types (the set of all type-level functions that accept 2 types).

  // Higher-Order Kinds
  // (* => *) => * — The kind of type constructors that accept a type constructor of kind * => *.

  // Kinds: *
  def method1[A]: A = ???
  val ms: String    = method1[String]
  val mi: Int       = method1[Int]

  // Kinds: * => *
  def method2[A[_]]: A[_] = ???
  val ml: List[_]         = method2[List]
  val mo: Option[_]       = method2[Option]

  // Kinds: [*, *] => *
  def method3[A[_, _]]: A[_, _] = ???
  val me: Either[_, _]          = method3[Either]
  val mt2: Tuple2[_, _]         = method3[Tuple2]

  // Kinds: [*, *, *] => *
  def method4[A[_, _, _]]: A[_, _, _] = ???
  val mt3: Tuple3[_, _, _]            = method4[Tuple3]

  // Kinds : (* => *) => *
  trait StackModule[F[_]] {
    def empty[A]: F[A]
    def push[A](a: A, s: F[A]): F[A]
    def pop[A](s: F[A]): Option[(A, F[A])]
  }
  val ListStack = new StackModule[List] {
    def empty[A]                            = Nil
    def push[A](a: A, as: List[A]): List[A] = a :: as
    def pop[A](s: List[A]): Option[(A, List[A])] = s match {
      case Nil     => None
      case a :: as => Some((a, as))
    }
  }

}
