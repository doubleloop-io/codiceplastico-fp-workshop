package day1.solutions

import scala.io.StdIn._
import cats.data._
import cats.implicits._
import cats.effect.IO
import monocle.Lens
import monocle.macros.GenLens

object Round10 {
  class Game {
    import Domain._
    import Logic._

    object Domain {

      case class Delta(x: Int, y: Int)

      sealed trait Direction {
        val delta: Delta
      }
      case class Upward(delta: Delta = Delta(-1, 0))   extends Direction
      case class Downward(delta: Delta = Delta(1, 0))  extends Direction
      case class Rightward(delta: Delta = Delta(0, 1)) extends Direction
      case class Leftward(delta: Delta = Delta(0, -1)) extends Direction

      case class Position(x: Int, y: Int)

      object Position {
        val x = GenLens[Position](_.x)
        val y = GenLens[Position](_.y)

        val origin: Position = Position(0, 0)
      }

      case class Player(name: String, position: Position)

      object Player {
        val name     = GenLens[Player](_.name)
        val position = GenLens[Player](_.position)

        def begin(name: String) = Player(name, Position.origin)
      }

      case class Field(grid: Vector[Vector[String]])

      object Field {
        val grid = GenLens[Field](_.grid)

        def mk20x20 =
          Field(Vector.fill(20, 20)("-"))
      }

      case class GameWorld(player: Player, field: Field)

      object GameWorld {
        val player = GenLens[GameWorld](_.player)
        val field  = GenLens[GameWorld](_.field)
      }
    }

    object Logic {

      val enter = System.getProperty("line.separator")

      sealed trait Command
      case object Help                      extends Command
      case object Show                      extends Command
      case class Move(direction: Direction) extends Command
      case object NoOp                      extends Command
      case object Quit                      extends Command
      case class Bad(message: String)       extends Command

      type Message      = String
      type HandleResult = GameWorld Ior Message

      def initWorld(): IO[GameWorld] =
        askName()
          .map(name => GameWorld(Player.begin(name), Field.mk20x20))
          .flatMap(world => putLine("Use commands to play") *> IO.pure(world))

      def askName(): IO[String] =
        putLine("What is your name?")
          .flatMap(_ => getLine().map(_.trim))
          .flatMap(name => putLine(s"Hello, $name, welcome to the game!") *> IO.pure(name))

      def gameLoop(world: GameWorld): IO[Unit] =
        gameStep(world).flatMap {
          case Some(w) => gameLoop(w)
          case None    => IO.unit
        }

      def gameStep(world: GameWorld): IO[Option[GameWorld]] =
        getLine()
          .map(parse)
          .map(dispatch(world, _))
          .flatMap(handle)

      def parse(line: String): Command =
        if (line.length > 0) {
          val words = line.trim.toLowerCase.split("\\s+")
          words(0) match {
            case "help" => Help
            case "show" => Show
            case "move" =>
              if (words.length < 2) {
                Bad("Missing direction")
              } else {
                words(1) match {
                  case "up"    => Move(Upward())
                  case "down"  => Move(Downward())
                  case "right" => Move(Rightward())
                  case "left"  => Move(Leftward())
                  case _       => Bad("Unknown direction")
                }
              }
            case "quit" => Quit
            case _      => Bad("Unknown command")
          }
        } else NoOp

      def dispatch(world: GameWorld, command: Command): HandleResult =
        command match {
          case Help            => Ior.both(world, renderHelp())
          case Show            => Ior.both(world, renderWorld(world))
          case Move(direction) => move(world, direction).fold(Ior.both(world, _), Ior.left(_))
          case NoOp            => Ior.left(world)
          case Bad(message)    => Ior.both(world, message)
          case Quit            => Ior.right(renderQuit(world))
        }

      def handle(result: HandleResult): IO[Option[GameWorld]] =
        result.fold(
          world => continue(world),
          message => putLine(message) *> end,
          (world, message) => putLine(message) *> continue(world)
        )

      def move(world: GameWorld, direction: Direction): Either[String, GameWorld] =
        position
          .modifyF(newPosition(world, direction.delta, _))(world)
          .toRight("Invalid direction")

      def newPosition(world: GameWorld, delta: Delta, current: Position): Option[Position] = {
        val next = Position(current.x + delta.x, current.y + delta.y)
        cell(world, next) *> Some(next)
      }

      def cell(world: GameWorld, position: Position): Option[String] =
        grid
          .get(world)
          .lift(position.x)
          .flatMap(_.lift(position.y))

      def renderQuit(world: GameWorld): String =
        s"Bye bye ${name.get(world)}!"

      def renderHelp(): String =
        s"""|
         |Valid commands:
         |
         | help
         | show
         | move <up|down|left|right>
         | quit
         |""".stripMargin

      def renderWorld(world: GameWorld): String = {
        val playerRow = grid
          .get(world)(x.get(world))
          .updated(y.get(world), "x")
        val updated = grid
          .get(world)
          .updated(x.get(world), playerRow)

        enter + updated.map(_.mkString(" ")).mkString(enter) + enter
      }

      def end: IO[Option[GameWorld]]                        = IO.pure(None)
      def continue(world: GameWorld): IO[Option[GameWorld]] = IO.pure(Some(world))

      def name: Lens[GameWorld, String] =
        GameWorld.player.composeLens(Player.name)

      def position: Lens[GameWorld, Position] =
        GameWorld.player.composeLens(Player.position)

      def x: Lens[GameWorld, Int] =
        GameWorld.player.composeLens(Player.position).composeLens(Position.x)

      def y: Lens[GameWorld, Int] =
        GameWorld.player.composeLens(Player.position).composeLens(Position.y)

      def grid: Lens[GameWorld, Vector[Vector[String]]] =
        GameWorld.field.composeLens(Field.grid)

      def getLine(): IO[String] =
        IO(readLine())

      def putLine(s: String): IO[Unit] =
        IO(println(s))
    }

    def safeApp(): IO[Unit] =
      initWorld()
        .flatMap(world => gameLoop(world))

    def run(): Unit =
      safeApp()
        .unsafeRunSync()
  }
}
