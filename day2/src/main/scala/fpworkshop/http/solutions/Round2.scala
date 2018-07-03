package fpworkshop.day2.http.solutions

object Round2 {
  // INFO: Combine two routes in one app

  type HttpApp = Request => Response

  val hello: HttpApp = {
    case Request(POST, Uri("/hello"), name) =>
      Response(OK, s"Hello, $name!")
  }

  val ciao: HttpApp = {
    case Request(POST, Uri("/ciao"), name) =>
      Response(OK, s"Ciao, $name!")
  }

  val app: HttpApp = combine(hello, ciao)

  def combine(x: HttpApp, y: HttpApp): HttpApp = { req =>
    try x(req)
    catch {
      case e: MatchError => y(req)
    }
  }
}
