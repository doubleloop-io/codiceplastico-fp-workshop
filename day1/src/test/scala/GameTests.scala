package day1

import minitest._

import java.io.ByteArrayOutputStream
import java.io.StringReader

import day1._

object GameTests extends SimpleTestSuite {

  test("enter then quit") {
    val result = execute(input("Luke"), input("quit"))

    val expected =
      "What is your name?" + enter +
        "Hello, Luke, welcome to the game!" + enter +
        "Use commands to play" + enter +
        "Bye bye Luke!" + enter

    assertEquals(result, expected)
  }

  // test("invoke help") {
  //   val result = execute(input("Luke"), input("help"), input("quit"))

  //   val expected =
  //     "What is your name?" + enter +
  //        "Hello, Luke, welcome to the game!" + enter +
  //        "Use commands to play" + enter +
  //        "" + enter +
  //        "Valid commands:" + enter +
  //        "" + enter +
  //        " help" + enter +
  //        " show" + enter +
  //        " move <up"down"left"right>" + enter +
  //        " quit" + enter +
  //        "" + enter +
  //        "Bye bye Luke!" + enter

  //   assertEquals(result, expected)
  // }

  test("invoke show") {
    val result = execute(input("Luke"), input("show"), input("quit"))

    val expected =
      "What is your name?" + enter +
        "Hello, Luke, welcome to the game!" + enter +
        "Use commands to play" + enter +
        "" + enter +
        "x - - - - - - - - - - - - - - - - - - -" + enter +
        "- - - - - - - - - - - - - - - - - - - -" + enter +
        "- - - - - - - - - - - - - - - - - - - -" + enter +
        "- - - - - - - - - - - - - - - - - - - -" + enter +
        "- - - - - - - - - - - - - - - - - - - -" + enter +
        "- - - - - - - - - - - - - - - - - - - -" + enter +
        "- - - - - - - - - - - - - - - - - - - -" + enter +
        "- - - - - - - - - - - - - - - - - - - -" + enter +
        "- - - - - - - - - - - - - - - - - - - -" + enter +
        "- - - - - - - - - - - - - - - - - - - -" + enter +
        "- - - - - - - - - - - - - - - - - - - -" + enter +
        "- - - - - - - - - - - - - - - - - - - -" + enter +
        "- - - - - - - - - - - - - - - - - - - -" + enter +
        "- - - - - - - - - - - - - - - - - - - -" + enter +
        "- - - - - - - - - - - - - - - - - - - -" + enter +
        "- - - - - - - - - - - - - - - - - - - -" + enter +
        "- - - - - - - - - - - - - - - - - - - -" + enter +
        "- - - - - - - - - - - - - - - - - - - -" + enter +
        "- - - - - - - - - - - - - - - - - - - -" + enter +
        "- - - - - - - - - - - - - - - - - - - -" + enter +
        "" + enter +
        "Bye bye Luke!" + enter

    assertEquals(result, expected)
  }

  // test("move downx2, rightx2, upx2, leftx2") {
  //   val result = execute(
  //     input("Luke"),
  //     input("move down"),
  //     input("move down"),
  //     input("move right"),
  //     input("move right"),
  //     input("move up"),
  //     input("move up"),
  //     input("move left"),
  //     input("move left"),
  //     input("show"),
  //     input("quit")
  //   )

  //   val expected =
  //     "What is your name?
  //        "Hello, Luke, welcome to the game!
  //        "Use commands to play
  //        "
  //        "x - - - - - - - - - - - - - - - - - - -
  //        "- - - - - - - - - - - - - - - - - - - -
  //        "- - - - - - - - - - - - - - - - - - - -
  //        "- - - - - - - - - - - - - - - - - - - -
  //        "- - - - - - - - - - - - - - - - - - - -
  //        "- - - - - - - - - - - - - - - - - - - -
  //        "- - - - - - - - - - - - - - - - - - - -
  //        "- - - - - - - - - - - - - - - - - - - -
  //        "- - - - - - - - - - - - - - - - - - - -
  //        "- - - - - - - - - - - - - - - - - - - -
  //        "- - - - - - - - - - - - - - - - - - - -
  //        "- - - - - - - - - - - - - - - - - - - -
  //        "- - - - - - - - - - - - - - - - - - - -
  //        "- - - - - - - - - - - - - - - - - - - -
  //        "- - - - - - - - - - - - - - - - - - - -
  //        "- - - - - - - - - - - - - - - - - - - -
  //        "- - - - - - - - - - - - - - - - - - - -
  //        "- - - - - - - - - - - - - - - - - - - -
  //        "- - - - - - - - - - - - - - - - - - - -
  //        "- - - - - - - - - - - - - - - - - - - -
  //        "
  //        "Bye bye Luke!

  //   assertEquals(result, expected)
  // }

  test("move without direction") {
    val result = execute(
      input("Luke"),
      input("move"),
      input("quit")
    )

    val expected =
      "What is your name?" + enter +
        "Hello, Luke, welcome to the game!" + enter +
        "Use commands to play" + enter +
        "Missing direction" + enter +
        "Bye bye Luke!" + enter

    assertEquals(result, expected)
  }

  test("move out of grid (down)") {
    val down20times = Array.fill(20)(input("move down"))
    val all         = input("Luke") +: down20times :+ input("quit")
    val result      = execute(all: _*)

    val expected =
      "What is your name?" + enter +
        "Hello, Luke, welcome to the game!" + enter +
        "Use commands to play" + enter +
        "Invalid direction" + enter +
        "Bye bye Luke!" + enter

    assertEquals(result, expected)
  }

  test("move out of grid (up)") {
    val result = execute(
      input("Luke"),
      input("move up"),
      input("quit")
    )

    val expected =
      "What is your name?" + enter +
        "Hello, Luke, welcome to the game!" + enter +
        "Use commands to play" + enter +
        "Invalid direction" + enter +
        "Bye bye Luke!" + enter

    assertEquals(result, expected)
  }

  test("move out of grid (left)") {
    val result = execute(
      input("Luke"),
      input("move left"),
      input("quit")
    )

    val expected =
      "What is your name?" + enter +
        "Hello, Luke, welcome to the game!" + enter +
        "Use commands to play" + enter +
        "Invalid direction" + enter +
        "Bye bye Luke!" + enter

    assertEquals(result, expected)
  }

  test("move out of grid (right)") {
    val right20times = Array.fill(20)(input("move right"))
    val all          = input("Luke") +: right20times :+ input("quit")
    val result       = execute(all: _*)

    val expected =
      "What is your name?" + enter +
        "Hello, Luke, welcome to the game!" + enter +
        "Use commands to play" + enter +
        "Invalid direction" + enter +
        "Bye bye Luke!" + enter

    assertEquals(result, expected)
  }

  test("unknown command") {
    val result = execute(
      input("Luke"),
      input("asdf"),
      input("quit")
    )

    val expected =
      "What is your name?" + enter +
        "Hello, Luke, welcome to the game!" + enter +
        "Use commands to play" + enter +
        "Unknown command" + enter +
        "Bye bye Luke!" + enter

    assertEquals(result, expected)
  }

  test("unknown move direction") {
    val result = execute(
      input("Luke"),
      input("move qwerty"),
      input("quit")
    )

    val expected =
      "What is your name?" + enter +
        "Hello, Luke, welcome to the game!" + enter +
        "Use commands to play" + enter +
        "Unknown direction" + enter +
        "Bye bye Luke!" + enter

    assertEquals(result, expected)
  }

  private def input(value: String): String =
    value + enter

  private def execute(inputs: String*): String = {
    val input = new StringReader(inputs.mkString(""))
    val out   = new ByteArrayOutputStream
    Console.withIn(input) {
      Console.withOut(out) {
        new Game().run()
      }
    }
    out.toString
  }

  private val enter: String = System.getProperty("line.separator")
}
