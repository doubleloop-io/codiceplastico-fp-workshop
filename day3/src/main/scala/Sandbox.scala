package day3

import cats._
import cats.data._
import cats.implicits._
import cats.effect._

object Sandbox {

  object Validation {
    lazy val enter = System.getProperty("line.separator")

    type ValidationResult[A] = ValidatedNel[ValidationError, A]

    implicit class ValidationResultOps[A](actual: ValidationResult[A]) {

      def toMonadError[F[_]](implicit ME: MonadError[F, Throwable]): F[A] =
        actual.fold(
          e => ME.raiseError(ValidationErrorException(e.toList: _*)),
          v => ME.pure(v)
        )
    }

    sealed trait ValidationError {
      def errorMessage: String
    }

    final case class ValidationErrorException(errors: ValidationError*)
        extends Exception("Error list:" + enter + errors.map("- " + _.errorMessage).mkString(enter))

    case class EmptyString(fieldName: String) extends ValidationError {
      def errorMessage: String = s"The $fieldName cannot be empty."
    }
    case class InvalidCharsString(fieldName: String, value: String) extends ValidationError {
      def errorMessage: String = s"The $fieldName cannot contain special characters: $value."
    }
    case class NegativeNumber(fieldName: String, value: Int) extends ValidationError {
      def errorMessage: String = s"The $fieldName cannot be negative: $value."
    }

    object Checkers {

      def valid[A](value: A): ValidationResult[A] =
        value.validNel

      def checkNotEmpty(value: String, fieldName: String): ValidationResult[String] =
        Either
          .cond(value.trim.nonEmpty, value, EmptyString(fieldName))
          .toValidatedNel

      def checkAlphanumeric(value: String, fieldName: String): ValidationResult[String] =
        Either
          .cond(value.matches("^[a-zA-Z0-9\\s]+$"), value, InvalidCharsString(fieldName, value))
          .toValidatedNel

      def checkPositive(value: Int, fieldName: String): ValidationResult[Int] =
        Either
          .cond(value >= 0, value, NegativeNumber(fieldName, value))
          .toValidatedNel
    }
  }

  object Models {

    import Validation._
    import Validation.Checkers._

    case class Item(name: String, count: Int)

    object Item {

      def createF[F[_]](name: String, count: Int)(implicit ME: MonadError[F, Throwable]): F[Item] =
        create(name, count).toMonadError[F]

      def create(name: String, count: Int): ValidationResult[Item] =
        (
          validateName(name),
          validateCount(count),
        ).mapN(Item.apply)

      def validateName(name: String): ValidationResult[String] =
        checkNotEmpty(name, "name")
          .andThen(checkAlphanumeric(_, "name"))

      def validateCount(count: Int): ValidationResult[Int] =
        checkPositive(count, "count")
    }
  }

  import Models._

  type Result[A] = IO[A]

  class Console[F[_]: Sync]() {
    def getLine(): F[String]           = Sync[F].delay(io.StdIn.readLine())
    def putLine(line: String): F[Unit] = Sync[F].delay(println(line))
  }

  class ItemRepository[F[_]: Sync]() {

    private var storage = Map.empty[String, Item]

    def load(id: String): F[Item] =
      storage
        .get(id)
        .fold(Sync[F].raiseError[Item](new Exception(s"Missing item: $id")))(Sync[F].pure)

    def save(id: String, item: Item): F[Item] = Sync[F].delay {
      storage = storage + (id -> item)
      item
    }
  }

  class ItemService[F[_]: Sync](repo: ItemRepository[F]) {

    def create(name: String, count: Int): F[Item] =
      Sync[F].flatMap(Item.createF(name, count)(Sync[F]))(item => repo.save(name, item))

    def checkout(id: String, count: Int): F[Item] =
      modify(id, i => i.copy(count = i.count - count))

    def checkin(id: String, count: Int): F[Item] =
      modify(id, i => i.copy(count = i.count + count))

    def rename(id: String, name: String): F[Item] =
      modify(id, i => i.copy(name = name))

    private def modify(id: String, f: Item => Item): F[Item] =
      Sync[F].flatMap(repo.load(id))(item => repo.save(id, f(item)))
  }

  def demo() = {
    val _repo    = new ItemRepository[Result]()
    val _console = new Console[Result]()
    val _service = new ItemService[Result](_repo)

    val p = for {
      i <- _service.create("", -100)
      // i1 <- _repo.load("minkia")
      _ <- _console.putLine("saved")
    } yield i

    println("ciao!")
    println(p.attempt.unsafeRunSync())

  }
}
