package day2.http

object Round1 {
  // GOAL: Define a simple http service

  type HttpApp = Request => Response

  // TODO: Define two routes:
  // 1. a route that match POST "/hello"
  // with a name in the body and produces a response
  // OK with che body "Hello, {name}!".
  // 2. a fallback route that always match and produces a NotFound response
  // See the test for more info.

  val app: HttpApp = {
    ???
  }

  // Below there are some usage examples.
  object ModelExamples {

    val getReq = Request(GET, Uri("/some/path"))
    val postReq = Request(POST, Uri("/some/other/path"), "The body is a Stirng")

    val okRes = Response(OK)
    val okRes2 = Response(OK, "Once again, the body is a String")
    val badRes = Response(BadRequest)
  }

  object MatchExamples {

    // Take advantage of the pattern match on the input parameters
    // to discriminate the application of the function
    val isEven: PartialFunction[Int, String] = {
      case x if x % 2 == 0 => x + " is even"
    }

    val s1 = isEven(6) // "6 is even"
    val s2 = isEven(3) // throws a MatchError
  }
}
