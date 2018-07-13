package day3.inventory

import java.util.UUID
import minitest._

import day3.solutions.inventory._
import day3.solutions.inventory.Models._
import day3.solutions.inventory.interpreter.itemservice._

object ExamplesTests extends InventorySuite {

  implicit val rndId          = fakeRandomId()
  implicit val console        = fakeConsole()
  implicit val itemRepository = fakeItemRepository()

  val id   = UUID.randomUUID()
  val init = TestState(id)

  test("demo ok") {
    val program = Examples.demoOk[TestResult]
    val result  = runTestResult(program, init)

    assertRight(result) { ts =>
      assertEquals(ts.output.size, 6)
      assertEquals(ts.items(id), Item(id, "pens", 12, false))
    }
  }

  test("demo bad name") {
    val program = Examples.demoBad[TestResult]
    val result  = runTestResult(program, init)

    assertLeft(result) { vr =>
      assertEquals(
        vr,
        ErrorList(
          InvalidCharsString("name", "@books!"),
          NegativeNumber("count", -5)
        )
      )
    }
  }

  test("demo not found") {
    val program = Examples.demoNotFound[TestResult]

    intercept[NoSuchElementException] {
      val result = runTestResult(program, init)
    }
  }
}
