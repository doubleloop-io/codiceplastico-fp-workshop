package day2.validation

import minitest._

import day2.validation.solutions.Round3._

object Round3Tests extends SimpleTestSuite {

  test("check int gt zero") {
    assertEquals(checkGtZero(100), Right(100))
    assertEquals(checkGtZero(0), Left(List(TooSmall)))
    assertEquals(checkGtZero(-340), Left(List(TooSmall)))
  }

  test("check string not empty") {
    assertEquals(checkNotEmpty("ciao"), Right("ciao"))
    assertEquals(checkNotEmpty(""), Left(List(Empty)))
  }

  test("check string is an int") {
    assertEquals(checkInt("123"), Right(123))
    assertEquals(checkInt("ciao"), Left(List(NotInteger)))
  }

  test("check string is a positive int") {
    assertEquals(checkNumber("123"), Right(123))
    assertEquals(checkNumber("-123"), Left(List(TooSmall)))
    assertEquals(checkNumber("ciao"), Left(List(NotInteger)))
  }

  test("check person") {
    assertEquals(checkPerson(("Matteo", "18")), Right(Person("Matteo", 18)))
    assertEquals(checkPerson(("", "18")), Left(List(Empty)))
    assertEquals(checkPerson(("Matteo", "-18")), Left(List(TooSmall)))
    assertEquals(checkPerson(("Matteo", "abc")), Left(List(NotInteger)))
  }

  // test("check person (many error)") {
  //   assertEquals(checkPerson(("", "-18")), Left(List(Empty, TooSmall)))
  //   assertEquals(checkPerson(("", "abc")), Left(List(Empty, NotInteger)))
  // }

}
