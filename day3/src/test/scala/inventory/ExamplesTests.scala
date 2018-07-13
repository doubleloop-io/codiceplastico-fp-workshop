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

  test("demo ok") {
    val id   = UUID.randomUUID()
    val init = TestState(id)

    val prog1  = Examples.demoOk[TestResult]
    val result = runTestResult(prog1, init)

    assertRight(result) { ts =>
      assertEquals(ts.output.size, 6)
      assertEquals(ts.items(id), Item(id, "pens", 12, false))
    }
  }

  test("demo bad name") {
    val id   = UUID.randomUUID()
    val init = TestState(id)

    val prog1  = Examples.demoBadName[TestResult]
    val result = runTestResult(prog1, init)

    assertLeft(result) { vr =>
      assertEquals(vr, ErrorList(InvalidCharsString("name", "@books!")))
    }
  }
}
