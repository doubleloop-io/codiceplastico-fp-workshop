package day2.fpworkshop.http

import cats._
import cats.data._
import cats.implicits._
import cats.effect._

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Translator {

  def text(value: String): String = value match {
    case "one"            => "uno"
    case "Hello, matteo!" => "Hola, matteo!"
    case ""               => ""
    case _                => "aslkifjasdlkfjasdlgfkjasd"
  }

  def future(value: String): Future[String] =
    Future.successful(text(value))

  def poli[F[_]: Monad](value: String): F[String] = Monad[F].pure(text(value))

  def poliSync[F[_]: Sync](value: String): F[String] =
    Sync[F].delay(text(value))
}
