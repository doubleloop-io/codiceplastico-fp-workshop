package day3.solutions.inventory.interpreter

import cats.effect.Sync
import java.util.UUID

import day3.solutions.inventory.Config
import day3.solutions.inventory.Models._
import day3.solutions.inventory.ItemRepository
import day3.solutions.inventory.ItemRepository.ItemNotFoundException

trait ItemRepositoryInstances {

  implicit def itemRepository[F[_]: Sync: Stateful]: ItemRepository[F] = new ItemRepository[F] {

    private val S = Sync[F]
    import S._

    private val SF = Stateful[F]
    import SF._

    def load(id: ItemId): F[Item] =
      flatMap(get)(s => s.items.get(id).fold(raiseError[Item](new ItemNotFoundException(id)))(pure))

    def save(id: ItemId, item: Item): F[Item] =
      map(modify(s => s.copy(items = s.items + (id -> item))))(_ => item)
  }

  object redis {
    import cats.mtl._

    implicit def redisItemRepository[F[_]: Sync](implicit AA: ApplicativeAsk[F, Config]): ItemRepository[F] =
      new ItemRepository[F] {

        private val S = Sync[F]
        import S._

        import com.redis._
        import com.redis._
        import serialization._
        import Parse.Implicits.parseByteArray

        private lazy val client: F[RedisClient] = mkClient()

        def load(id: ItemId): F[Item] =
          flatMap(client) { cli =>
            cli
              .get[Array[Byte]](formatId(id))
              .map(bs => Serializer.deserialize[Item](bs))
              .fold(raiseError[Item](new ItemNotFoundException(id)))(pure)
          }

        def save(id: ItemId, item: Item): F[Item] =
          flatMap(client) { cli =>
            val isOK = cli.set(formatId(id), Serializer.serialize(item))
            if (isOK) pure(item) else raiseError[Item](new Exception(s"Can't write to redis: $item"))
          }

        private def formatId(id: ItemId): String =
          id.toString.replace("-", "")

        private def mkClient(): F[RedisClient] =
          flatMap(AA.ask)(c => delay(new RedisClient(c.redisHost, c.redisPort)))

        object Serializer {
          import java.io._

          def serialize[T <: Serializable](obj: T): Array[Byte] = {
            val byteOut = new ByteArrayOutputStream()
            val objOut  = new ObjectOutputStream(byteOut)
            objOut.writeObject(obj)
            objOut.close()
            byteOut.close()
            byteOut.toByteArray
          }

          def deserialize[T <: Serializable](bytes: Array[Byte]): T = {
            val byteIn = new ByteArrayInputStream(bytes)
            val objIn  = new ObjectInputStream(byteIn)
            val obj    = objIn.readObject().asInstanceOf[T]
            byteIn.close()
            objIn.close()
            obj
          }
        }
      }
  }

}

object itemrepository extends ItemRepositoryInstances
