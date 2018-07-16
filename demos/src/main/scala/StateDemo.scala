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

    def runA(s: S): A =
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

  case class Player(health: Int)

  def fight(damage: Int): State[Player, Int] = State { player =>
    val newHealth = player.health - damage
    val newPlayer = player.copy(health = newHealth)
    (newPlayer, newHealth)
  }

  def elixir(life: Int): State[Player, Int] =
    for {
      player    <- State.get
      newHealth = player.health + life
      newPlayer = player.copy(health = newHealth)
      _         <- State.set(newPlayer)
    } yield newHealth

  def bomb(power: Int): State[Player, Int] =
    State
      .modify[Player](s => s.copy(health = s.health - (power * 100)))
      .flatMap(_ => State.get.map(_.health))

  val program: State[Player, Int] = for {
    _      <- fight(20)
    _      <- elixir(50)
    health <- bomb(10)
  } yield health

  def run = {

    val init = Player(100)
    println(program.run(init))
    println(program.runS(init))
    println(program.runA(init))

  }
}
