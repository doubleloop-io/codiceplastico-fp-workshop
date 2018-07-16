package demos

import cats._
import cats.data._
import cats.data.Validated._
import cats.effect._

import scala.util.Try
import scala.util.Success
import scala.util.Failure

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object StaticVsDynamicErrorDemo {

  object StaticErrorHandling {

    def computeEither(magicValue: Int): Either[String, Int] =
      if (magicValue > 0) Right(magicValue * 2)
      else Left("invalid number")

    def runEither() = {
      val value = computeEither(-10)
      println("After compute")
      // there isn't a way to evaluate the effect
      // and receive an unhandled exception.
      // the error is a materialized value (string)
      // so we explicit it in the type signature
      // and we need to handle it explicitly
      value.getOrElse(42)
      println("After effect evaluation")
    }

    def computeEitherThrowable(magicValue: Int): Either[Throwable, Int] =
      if (magicValue > 0) Right(magicValue * 2)
      else Left(new Exception("invalid number"))

    def runEitherThrowable() = {
      val value = computeEitherThrowable(-10)
      println("After compute")
      // there isn't a way to evaluate the effect
      // and receive an unhandled exception.
      // the error is a materialized value (throwable)
      // so we explicit it in the type signature
      // and we need to handle it explicitly.
      value.getOrElse(42)
      println("After effect evaluation")
    }

    def computeValidated(magicValue: Int): Validated[String, Int] =
      if (magicValue > 0) Valid(magicValue * 2)
      else Invalid("invalid number")

    def runValidated() = {
      val value = computeValidated(-10)
      println("After compute")
      // there isn't a way to evaluate the effect
      // and receive an unhandled exception.
      // the error is a materialized value (string)
      // so we explicit it in the type signature
      // and we need to handle it explicitly.
      value.getOrElse(42)
      println("After effect evaluation")
    }

  }

  object DynamicErrorHandling {

    def computeIO(magicValue: Int): IO[Int] =
      if (magicValue > 0) IO.pure(magicValue * 2)
      else IO(throw new Exception("invalid number"))

    def runIO() = {
      val value = computeIO(-10)
      println("After compute")
      value.unsafeRunSync()
      println("After effect evaluation")
    }

    def computeTry(magicValue: Int): Try[Int] =
      if (magicValue > 0) Success(magicValue * 2)
      else Failure(new Exception("invalid number"))

    def runTry() = {
      val value = computeTry(-10)
      println("After compute")
      value.get
      println("After effect evaluation")
    }

    def computeFuture(magicValue: Int): Future[Int] =
      if (magicValue > 0) Future.successful(magicValue * 2)
      else Future.failed(new Exception("invalid number"))

    def runFuture() = {
      val value = computeFuture(-10)
      println("After compute")
      Await.result(value, 2.seconds)
      println("After effect evaluation")
    }

  }

  object DelayErrorHandlingType {

    type Throwing[F[_]] = MonadError[F, Throwable]

    object Throwing {
      def apply[F[_]](implicit T: Throwing[F]): Throwing[F] = T
    }

    def compute[F[_]: Throwing](magicValue: Int): F[Int] =
      if (magicValue > 0) Throwing[F].pure(magicValue * 2)
      else Throwing[F].raiseError(new Exception("invalid number"))

    def run() = {
      type ThrowableOr[A] = EitherT[Id, Throwable, A]
      compute[ThrowableOr](-10).getOrElse(42)
      println("After EitherT")

      compute[IO](-10).unsafeRunSync()
      println("After IO")
    }
  }
}
