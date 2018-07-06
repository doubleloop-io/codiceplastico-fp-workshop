package demos

object ComposeDemo {

  val parse: String => Int =
    s => s.toInt

  val reciprocal: Int => Double =
    i => 1.0 / i

  val classic: String => Double =
    reciprocal.compose(parse)

  val inverted: String => Double =
    parse.andThen(reciprocal)

}
