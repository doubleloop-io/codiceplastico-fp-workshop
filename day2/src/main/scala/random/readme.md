# Functional Random Generators

Imagine that our API allow us to generate only integers

val rand = new scala.util.Random()
val value = rand.nextInt()

How to get random values for other domains, such as:
booleans, strings, pairs, lists, ... ?
