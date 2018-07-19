package demos

object TaglessFinalDemo {

  // Classic encoding w/ ADT

  sealed trait Exp
  case class Num(value: Int)         extends Exp
  case class Add(lhs: Exp, rhs: Exp) extends Exp
  case class Sub(lhs: Exp, rhs: Exp) extends Exp
  case class Mul(lhs: Exp, rhs: Exp) extends Exp

  // 3 * 3 +1
  val classicProgram = Add(Mul(Num(3), Num(3)), Num(1))

  def classicEval(exp: Exp): Int = exp match {
    case Num(v)      => v
    case Add(el, er) => classicEval(el) + classicEval(er)
    case Sub(el, er) => classicEval(el) - classicEval(er)
    case Mul(el, er) => classicEval(el) * classicEval(er)
  }

  def classicRun = classicEval(classicProgram)

  // Tagless final encoding (first order)

  trait ExpAlg[A] {
    def num(value: Int): A
    def add(l: A, r: A): A
    def sub(l: A, r: A): A
    def mul(l: A, r: A): A
  }

  def taglessProgram[A](exp: ExpAlg[A]): A = {
    import exp._
    add(mul(num(3), num(3)), num(1))
  }

  object TaglessIntEval extends ExpAlg[Int] {
    def num(value: Int): Int     = value
    def add(l: Int, r: Int): Int = l + r
    def sub(l: Int, r: Int): Int = l - r
    def mul(l: Int, r: Int): Int = l * r
  }

  def taglessRun: Int = taglessProgram(TaglessIntEval)

  // Tagless final (high order)

  trait ExpF[F[_]] {
    def num(value: Int): F[Int]
    def add(l: F[Int], r: F[Int]): F[Int]
    def sub(l: F[Int], r: F[Int]): F[Int]
    def mul(l: F[Int], r: F[Int]): F[Int]
  }

  def taglessProgramF[F[_]](exp: ExpF[F]): F[Int] = {
    import exp._
    add(mul(num(3), num(3)), num(1))
  }

  import cats.implicits._
  import cats.effect._

  object TaglessIntEvalIO extends ExpF[IO] {
    def num(value: Int): IO[Int]             = IO.pure(value)
    def add(l: IO[Int], r: IO[Int]): IO[Int] = (l, r).mapN(_ + _)
    def sub(l: IO[Int], r: IO[Int]): IO[Int] = (l, r).mapN(_ - _)
    def mul(l: IO[Int], r: IO[Int]): IO[Int] = (l, r).mapN(_ * _)
  }

  def taglessRunF: IO[Int] = taglessProgramF(TaglessIntEvalIO)
}
