package day3.inventory.solutions

import java.util.UUID

import cats.data._

import day3.solutions.inventory._
import day3.solutions.inventory.Models._
import day3.solutions.inventory.Validation._
import day3.solutions.inventory.interpreter.itemservice._
import day3.solutions.inventory.ItemRepository.ItemNotFoundException

object ExamplesTests extends InventorySuite {

  val id   = ItemId(UUID.randomUUID())
  val init = TestState(id.value)

  test("demo ok") {
    implicit val rndId          = fakeRandomId()
    implicit val itemRepository = fakeItemRepository()

    val program = Examples.demoOk[TestResult]
    val result  = runTestResult(program, init)

    assertRight(result) { ts =>
      assertEquals(ts.items(id), Item(id, "pens", 12, false))
    }
  }

  test("demo bad name") {
    implicit val rndId          = fakeRandomId()
    implicit val itemRepository = fakeItemRepository()

    val program = Examples.demoBad[TestResult]
    val result  = runTestResult(program, init)

    assertLeft(result) { ex =>
      assertEquals(
        ex,
        ValidationErrorException(
          NonEmptyList(
            InvalidCharsString("name", "@books!"),
            List(NegativeNumber("count", -5))
          )
        )
      )
    }
  }

  test("demo not found") {
    implicit val rndId = fakeRandomId()
    implicit val repo  = fakeItemRepository_ItemNotFoundException()

    val program = Examples.demoNotFound[TestResult]
    val result  = runTestResult(program, init)

    assertLeft(result) { ex =>
      assertEquals(ex, ItemNotFoundException(id))
    }
  }
}
