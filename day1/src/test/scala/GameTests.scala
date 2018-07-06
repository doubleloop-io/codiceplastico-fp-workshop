package day1

import minitest._

import java.io.ByteArrayOutputStream
import java.io.StringReader

object GameTests extends SimpleTestSuite {

  test("enter then quit") {
    val result = execute(input("Luke"), input("quit"))

    val expected =
      s"""What is your name?
         |Hello, Luke, welcome to the game!
         |Enter a command:
         |Bye bye Luke!
         |""".stripMargin

    assertEquals(result, expected)
  }

  test("invoke help") {
    val result = execute(input("Luke"), input("help"), input("quit"))

    val expected =
      s"""What is your name?
         |Hello, Luke, welcome to the game!
         |Enter a command:
         |Valid commands:
         |
         | help
         | show
         | move <up|down|left|right>
         | quit
         |
         |Enter a command:
         |Bye bye Luke!
         |""".stripMargin

    assertEquals(result, expected)
  }

  test("invoke show") {
    val result = execute(input("Luke"), input("show"), input("quit"))

    val expected =
      s"""What is your name?
         |Hello, Luke, welcome to the game!
         |Enter a command:
         |
         |x | - | -
         |- | - | -
         |- | - | -
         |
         |Enter a command:
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
        Main.main(Array())
      }
    }
    out.toString
  }

  private val enter: String = System.getProperty("line.separator")
}
