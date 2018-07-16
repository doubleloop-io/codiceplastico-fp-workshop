package day2.validation

import minitest._

import day2.validation.solutions.Round7._

object Round7Tests extends SimpleTestSuite {

  test("check int gt zero") {
    assertEquals(checkGtZero(100), Success(100))
    assertEquals(checkGtZero(0), Fail(List(TooSmall)))
    assertEquals(checkGtZero(-340), Fail(List(TooSmall)))
  }

  test("check string not empty") {
    assertEquals(checkNotEmpty("ciao"), Success("ciao"))
    assertEquals(checkNotEmpty(""), Fail(List(Empty)))
  }

  test("check string is an int") {
    assertEquals(checkInt("123"), Success(123))
    assertEquals(checkInt("ciao"), Fail(List(NotInteger)))
  }

  test("check string is a positive int") {
    assertEquals(checkNumber("123"), Success(123))
    assertEquals(checkNumber("-123"), Fail(List(TooSmall)))
    assertEquals(checkNumber("ciao"), Fail(List(NotInteger)))
  }

  test("check person") {
    assertEquals(checkPerson(Map(("Matteo" -> "18"))), Success(Person("Matteo", 18)))
    assertEquals(checkPerson(Map((""       -> "18"))), Fail(List(Empty)))
    assertEquals(checkPerson(Map(("Matteo" -> "-18"))), Fail(List(TooSmall)))
    assertEquals(checkPerson(Map(("Matteo" -> "abc"))), Fail(List(NotInteger)))
  }

  test("check person (many error)") {
    assertEquals(checkPerson(Map(("" -> "-18"))), Fail(List(Empty, TooSmall)))
    assertEquals(checkPerson(Map(("" -> "abc"))), Fail(List(Empty, NotInteger)))
  }

}
