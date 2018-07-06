package day1

import minitest._

import java.io.ByteArrayOutputStream
import java.io.StringReader

import day1.solutions.Round3._

object Round3Tests extends SimpleTestSuite {

  test("enter then quit") {
    val result = execute(input("Luke"), input("quit"))

    val expected =
      s"""What is your name?
         |Hello, Luke, welcome to the game!
         |Use commands to play
         |Bye bye Luke!
         |""".stripMargin

    assertEquals(result, expected)
  }

  test("invoke help") {
    val result = execute(input("Luke"), input("help"), input("quit"))

    val expected =
      s"""What is your name?
         |Hello, Luke, welcome to the game!
         |Use commands to play
         |
         |Valid commands:
         |
         | help
         | show
         | move <up|down|left|right>
         | quit
         |
         |Bye bye Luke!
         |""".stripMargin

    assertEquals(result, expected)
  }

  test("invoke show") {
    val result = execute(input("Luke"), input("show"), input("quit"))

    val expected =
      s"""What is your name?
         |Hello, Luke, welcome to the game!
         |Use commands to play
         |
         |x | - | -
         |- | - | -
         |- | - | -
         |
         |Bye bye Luke!
         |""".stripMargin

    assertEquals(result, expected)
  }

  test("move downx2, rightx2, upx2, leftx2") {
    val result = execute(
      input("Luke"),
      input("move down"),
      input("move down"),
      input("move right"),
      input("move right"),
      input("move up"),
      input("move up"),
      input("move left"),
      input("move left"),
      input("show"),
      input("quit")
    )

    val expected =
      s"""What is your name?
         |Hello, Luke, welcome to the game!
         |Use commands to play
         |
         |x | - | -
         |- | - | -
         |- | - | -
         |
         |Bye bye Luke!
         |""".stripMargin

    assertEquals(result, expected)
  }

  test("move without direction") {
    val result = execute(
      input("Luke"),
      input("move"),
      input("quit")
    )

    val expected =
      s"""What is your name?
         |Hello, Luke, welcome to the game!
         |Use commands to play
         |Missing direction
         |Bye bye Luke!
         |""".stripMargin

    assertEquals(result, expected)
  }

  test("unknown command") {
    val result = execute(
      input("Luke"),
      input("asdf"),
      input("quit")
    )

    val expected =
      s"""What is your name?
         |Hello, Luke, welcome to the game!
         |Use commands to play
         |Unknown command
         |Bye bye Luke!
         |""".stripMargin

    assertEquals(result, expected)
  }

  test("unknown move direction") {
    val result = execute(
      input("Luke"),
      input("move qwerty"),
      input("quit")
    )

    val expected =
      s"""What is your name?
         |Hello, Luke, welcome to the game!
         |Use commands to play
         |Unknown direction
         |Bye bye Luke!
         |""".stripMargin

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