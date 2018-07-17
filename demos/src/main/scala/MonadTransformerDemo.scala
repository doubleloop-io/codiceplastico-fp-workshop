package demos

import cats._
import cats.effect._

object MonadTransformersDemo {

  case class User(id: Long)
  case class Address(street: String)

  object OneEffect {
    def findUserById(id: Long): IO[User] =
      IO.pure(User(id))

    def findAddressByUser(user: User): IO[Address] =
      IO.pure(Address("somewhere"))

    def findAddressByUserId(id: Long): IO[Address] =
      for {
        user    <- findUserById(id)
        address <- findAddressByUser(user)
      } yield address

    def run() =
      findAddressByUserId(42)
        .flatMap(address => IO(println(s"Found address: $address")))
        .unsafeRunSync()
  }

  object NestedEffects {

    def findUserById(id: Long): IO[Option[User]] =
      if (id == 42) IO.pure(Option(User(id)))
      else IO.pure(None)

    def findAddressByUser(user: User): IO[Option[Address]] =
      if (user.id == 42) IO.pure(Option(Address("somewhere")))
      else IO.pure(None)

    def findAddressByUserId(id: Long): IO[Option[Address]] =
      findUserById(id).flatMap { opt =>
        opt.fold(IO.pure(None: Option[Address])) { user =>
          findAddressByUser(user)
        }
      }

    def run() =
      findAddressByUserId(42)
        .flatMap { opt =>
          opt.fold(IO(println("Missing address or user."))) { address =>
            IO(println(s"Found address: $address"))
          }
        }
        .unsafeRunSync()
  }

  object FlattenedEffects {

    final case class OptionT[F[_], A](value: F[Option[A]]) {

      def map[B](f: A => B)(implicit F: Functor[F]): OptionT[F, B] =
        OptionT(F.map(value)(_.map(f)))

      def flatMap[B](f: A => OptionT[F, B])(implicit M: Monad[F]): OptionT[F, B] =
        OptionT(M.flatMap(value)(_.fold(M.pure[Option[B]](None))(a => f(a).value)))

      def fold[B](default: => B)(f: A => B)(implicit F: Functor[F]): F[B] =
        F.map(value)(_.fold(default)(f))
    }

    object OptionT {
      def some[F[_]: Monad, A](a: A): OptionT[F, A] =
        OptionT(Applicative[F].pure(Option(a)))

      def none[F[_]: Monad, A]: OptionT[F, A] =
        OptionT(Applicative[F].pure(None: Option[A]))
    }

    def findUserById(id: Long): OptionT[IO, User] =
      if (id == 42) OptionT.some[IO, User](User(id))
      else OptionT.none[IO, User]

    def findAddressByUser(user: User): OptionT[IO, Address] =
      if (user.id == 42) OptionT.some[IO, Address](Address("somewhere"))
      else OptionT.none[IO, Address]

    def findAddressByUserId(id: Long): OptionT[IO, Address] =
      for {
        user    <- findUserById(id)
        address <- findAddressByUser(user)
      } yield address

    def run() =
      findAddressByUserId(42)
        .fold("Missing address or user")(address => s"Found address: $address")
        .flatMap(message => IO(println(message)))
        .unsafeRunSync()
  }
}
