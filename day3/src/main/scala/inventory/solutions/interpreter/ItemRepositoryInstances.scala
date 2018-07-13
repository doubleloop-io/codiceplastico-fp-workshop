package day3.solutions.inventory.interpreter

import cats.effect.Sync
import java.util.UUID

import day3.solutions.inventory.Models._
import day3.solutions.inventory.ItemRepository
import day3.solutions.inventory.ItemRepository.ItemNotFoundException

trait ItemRepositoryInstances {

  implicit def itemRepository[F[_]: Sync]: ItemRepository[F] = new ItemRepository[F] {

    private val S = Sync[F]
    import S._

    private var storage = Map.empty[UUID, Item]

    def load(id: UUID): F[Item] =
      storage
        .get(id)
        .fold(raiseError[Item](new ItemNotFoundException(id)))(pure)

    def save(id: UUID, item: Item): F[Item] = delay {
      storage = storage + (id -> item)
      item
    }
  }

  def redisItemRepository[F[_]: Sync]: ItemRepository[F] = new ItemRepository[F] {

    private val S = Sync[F]
    import S._

    import com.redis._
    import com.redis._
    import serialization._
    import Parse.Implicits.parseByteArray

    val r = new RedisClient("localhost", 6379)

    def load(id: UUID): F[Item] = {
      val foi = delay {
        val obs = r.get[Array[Byte]](formatId(id))
        obs.map(bs => Serializer.deserialize[Item](bs))
      }

      flatMap(foi)(_.fold(raiseError[Item](new ItemNotFoundException(id)))(pure))
    }

    def save(id: UUID, item: Item): F[Item] = {
      val fb = delay {
        val bs = Serializer.serialize(item)
        r.set(formatId(id), bs)
      }

      map(fb)(_ => item)
    }

    private def formatId(id: UUID): String =
      id.toString.replace("-", "")

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

object itemrepository extends ItemRepositoryInstances
