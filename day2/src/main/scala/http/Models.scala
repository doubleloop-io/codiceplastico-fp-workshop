package day2.http

case class Uri(value: String)

trait Method
case object GET  extends Method
case object POST extends Method

trait Status
case object OK         extends Status
case object BadRequest extends Status
case object NotFound   extends Status

case class Request(
    method: Method,
    uri: Uri,
    // headers, version, ...
    body: String = ""
)

case class Response(
    status: Status,
    // headers, version, ...
    body: String = ""
)

object Translator {

  import scala.concurrent._
  import scala.concurrent.ExecutionContext.Implicits.global

  import cats._

  def italian(text: String): Future[String] = Future {
    text match {
      case "Hello, matteo!" => s"Ciao, matteo!"
    }
  }

  def italianM[F[_]: Monad](text: String): F[String] =
    Monad[F].pure(text match {
      case "Hello, matteo!" => s"Ciao, matteo!"
    })
}
