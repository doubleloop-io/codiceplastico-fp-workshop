package day2.http

object Round2 {
  // GOAL: Combine two routes in one app

  type HttpApp = Request => Response

  // TODO: Remove the fallback route
  val hello: HttpApp = {
    case Request(POST, Uri("/hello"), name) =>
      Response(OK, s"Hello, $name!")
    case _ =>
      Response(NotFound)
  }

  // TODO: Define a route that match POST "/ciao"
  // with a name in the body and produces a response
  // OK with che body "Ciao, {name}!".
  val ciao: HttpApp = ???

  val app: HttpApp = combine(hello, ciao)

  // TODO: Implements a logic that apply the routes in order
  // and stop on the first that match.
  def combine(first: HttpApp, second: HttpApp): HttpApp = ???
}
