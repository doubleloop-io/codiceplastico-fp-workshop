// package day2.validation

// import minitest._

// import day2.validation.Round2._

// object Round2Tests extends SimpleTestSuite {

//   test("check int gt zero") {
//     assertEquals(checkGtZero(100), Right(100))
//     assertEquals(checkGtZero(0), Left(List(TooSmall)))
//     assertEquals(checkGtZero(-340), Left(List(TooSmall)))
//   }

//   test("check string not empty") {
//     assertEquals(checkNotEmpty("ciao"), Right("ciao"))
//     assertEquals(checkNotEmpty(""), Left(List(Empty)))
//   }

//   test("check string is an int") {
//     assertEquals(checkInt("123"), Right(123))
//     assertEquals(checkInt("ciao"), Left(List(NotInteger)))
//   }

// }
