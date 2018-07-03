package day2.http.solutions

import day2.http._

object Round1 {
  // GOAL: Define a simple http service

  type HttpApp = Request => Response

  val app: HttpApp = {

    case Request(POST, Uri("/hello"), name) =>
      Response(OK, s"Hello, $name!")

    case _ =>
      Response(NotFound)

  }
}
