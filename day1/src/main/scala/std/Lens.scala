package day1.std

// basic lens definition
case class Lens[S, A](get: S => A, set: (S, A) => S) {

  def |->[B](that: Lens[A, B]): Lens[S, B] =
    composeLens(that)

  def composeLens[B](that: Lens[A, B]): Lens[S, B] = Lens(
    get = that.get.compose(get),
    set = (s, b) => set(s, that.set(get(s), b))
  )
}

/*

Alias for optics composition:
<-> == composeIso
?-> == composePrism
|-> == composeLens
|->> == composeTraversal

*/