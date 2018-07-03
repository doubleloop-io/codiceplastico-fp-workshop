package fpworkshop.day2.http.solutions

object Round1 {
  // INFO: Define a simple http service

  type HttpApp = Request => Response

  val helloWorld: HttpApp = {

    case Request(POST, Uri("/hello"), name) =>
      Response(OK, s"Hello, $name!")

    case _ =>
      Response(NotFound)

  }
}
