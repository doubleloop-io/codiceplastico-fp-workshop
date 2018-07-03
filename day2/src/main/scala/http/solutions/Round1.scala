package day2.fpworkshop.http.solutions

import day2.fpworkshop.http._

object Round1 {
  // INFO: Define a simple http service

  type HttpApp = Request => Response

  val app: HttpApp = {

    case Request(POST, Uri("/hello"), name) =>
      Response(OK, s"Hello, $name!")

    case _ =>
      Response(NotFound)

  }
}
