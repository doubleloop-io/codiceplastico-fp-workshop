package demos

object StateDemo {

  case class State[S, A](run: S => (S, A)) {
    def flatMap[B](f: A => State[S, B]): State[S, B] = State { s0 =>
      val (s1, a) = run(s0)
      f(a).run(s1)
    }

    def map[B](f: A => B): State[S, B] =
      flatMap(a => State.point(f(a)))

    def runS(s: S): S =
      run(s)._1

    def runV(s: S): A =
      run(s)._2
  }

  object State {
    def point[S, A](value: A): State[S, A] =
      State(s => (s, value))

    def get[S]: State[S, S] =
      State(s => (s, s))

    def set[S](s: S): State[S, Unit] =
      State(_ => (s, ()))

    def modify[S](f: S => S): State[S, Unit] =
      get.map(f).flatMap(set)
  }
}
