package demos

object LensDemo {

  case class Lens[S, A](get: S => A, set: (A, S) => S) {

    def |->[B](that: Lens[A, B]): Lens[S, B] =
      composeLens(that)

    def composeLens[B](that: Lens[A, B]): Lens[S, B] = Lens(
      get = that.get.compose(get),
      set = (b, s) => set(that.set(b, get(s)), s)
    )

    def modify(f: A => A): S => S =
      s => set(f(get(s)), s)
  }

  case class House(person: Person)
  case class Person(name: String, age: Int)

  val _person = Lens[House, Person](_.person, (v, old) => old.copy(person = v))
  val _name   = Lens[Person, String](_.name, (v, old) => old.copy(name = v))

  val p = Person("matteo", 35)
  val h = House(p)

  val n = _person |-> _name

  val newHouse = n.modify(old => old + " baglini")(h)
}
