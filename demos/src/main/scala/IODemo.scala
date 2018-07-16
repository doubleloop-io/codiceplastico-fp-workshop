package demos

object IODemo {

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

  def getLine(): IO[String]           = IO(io.StdIn.readLine())
  def putLine(line: String): IO[Unit] = IO(println(line))

  val askName: IO[String] =
    for {
      _    <- IO(println("Name?"))
      name <- IO(io.StdIn.readLine())
    } yield name

  def greetings(name: String): IO[Unit] =
    IO(println(s"Hi $name!"))

  val genSecret: IO[Int] =
    for {
      secret <- IO(new util.Random().nextInt.abs)
      _      <- IO(println(s"Your secret number is: $secret"))
    } yield secret

  val program: IO[Unit] =
    askName
      .flatMap(greetings)
      .flatMap(_ => genSecret)
      .flatMap(_ => IO.unit)

  def run() =
    program.unsafePerformIO()

}
