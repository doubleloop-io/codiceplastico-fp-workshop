package day1.std

class IO[A] private (op: () => A) {
  def unsafeRun(): A = op()

  def flatMap[B](f: A => IO[B]): IO[B] =
    IO(f(unsafeRun()).unsafeRun())

  def map[B](f: A => B): IO[B] =
    flatMap(a => IO(f(a)))
}

object IO {
  def unit(): IO[Unit]          = IO(())
  def apply[A](op: => A): IO[A] = new IO[A](() => op)
}
