package day2.fpworkshop

package object http {

  case class Uri(value: String)

  trait Method
  case object GET extends Method
  case object POST extends Method

  trait Status
  case object OK extends Status
  case object BadRequest extends Status
  case object NotFound extends Status

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

}
