package day1.std

class IO[A] private (op: () => A) {
  def unsafePerformIO(): A = op()

  def flatMap[B](f: A => IO[B]): IO[B] =
    IO(f(unsafePerformIO()).unsafePerformIO())

  def map[B](f: A => B): IO[B] =
    flatMap(a => IO(f(a)))

  def attempt(): IO[Either[Throwable, A]] =
    IO(
      try Right(unsafePerformIO())
      catch { case e: Throwable => Left(e) }
    )
}

object IO {
  def unit(): IO[Unit] =
    IO(())

  def apply[A](op: => A): IO[A] =
    new IO[A](() => op)

  def fail[A](e: Throwable): IO[A] =
    IO(throw e)
}
