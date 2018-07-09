package day1.solutions

import scala.io.StdIn._

object Round10 {
  class Game {
    import day1.std._
    import Domain._

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

        val x: Lens[Position, Int] = Lens(_.x, (v, s) => s.copy(x = v))
        val y: Lens[Position, Int] = Lens(_.y, (v, s) => s.copy(y = v))

        val origin: Position = Position(0, 0)
      }

      case class Player(name: String, position: Position)

      object Player {

        val name: Lens[Player, String]       = Lens(_.name, (v, s) => s.copy(name = v))
        val position: Lens[Player, Position] = Lens(_.position, (v, s) => s.copy(position = v))

        def begin(name: String) = Player(name, Position.origin)
      }

      case class Field(grid: Vector[Vector[String]])

      object Field {

        val grid: Lens[Field, Vector[Vector[String]]] = Lens(_.grid, (v, s) => s.copy(grid = v))

        def mk20x20 =
          Field(Vector.fill(20, 20)("-"))
      }

      case class GameWorld(player: Player, field: Field)

      object GameWorld {

        val player: Lens[GameWorld, Player] = Lens(_.player, (v, s) => s.copy(player = v))
        val field: Lens[GameWorld, Field]   = Lens(_.field, (v, s) => s.copy(field = v))

      }
    }

    object Logic {
      import Domain._

      val enter = System.getProperty("line.separator")

      sealed trait Command
      case object Help                      extends Command
      case object Show                      extends Command
      case class Move(direction: Direction) extends Command
      case object NoOp                      extends Command
      case object Quit                      extends Command
      case class Bad(message: String)       extends Command

      trait HandleResult
      case class Continue(world: GameWorld, message: Option[String]) extends HandleResult
      case class End(essage: String)                                 extends HandleResult

      def initWorld(): IO[GameWorld] =
        askName()
          .map(name => GameWorld(Player.begin(name), Field.mk20x20))
          .flatMap(world => putLine("Use commands to play").map(_ => world))

      def askName(): IO[String] =
        putLine("What is your name?")
          .flatMap(_ => getLine().map(_.trim))
          .flatMap(name => putLine(s"Hello, $name, welcome to the game!").map(_ => name))

      def gameLoop(world: GameWorld): Unit =
        gameStep(world) match {
          case Some(w) => gameLoop(w)
          case None    => ()
        }

      def gameStep(world: GameWorld): Option[GameWorld] =
        handle(dispatch(world, parse(readLine())))

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
          case Help => Continue(world, Some(renderHelp()))
          case Show => Continue(world, Some(renderWorld(world)))
          case Move(direction) =>
            move(world, direction).fold(m => Continue(world, Some(m)), Continue(_, None))
          case NoOp         => Continue(world, None)
          case Bad(message) => Continue(world, Some(message))
          case Quit         => End(renderQuit(world))
        }

      def handle(result: HandleResult): Option[GameWorld] =
        result match {
          case Continue(world, messageOpt) => {
            messageOpt.fold(())(println(_))
            continue(world)
          }
          case End(message) => {
            println(message)
            end
          }
        }

      def move(world: GameWorld, direction: Direction): Either[String, GameWorld] = {
        val newPosition = Position(
          x.get(world) + direction.delta.x,
          y.get(world) + direction.delta.y
        )

        cell(world, newPosition)
          .map(_ => position.set(newPosition, world))
          .toRight("Invalid direction")
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

      def end: Option[GameWorld]                        = None
      def continue(world: GameWorld): Option[GameWorld] = Some(world)

      def name: Lens[GameWorld, String] =
        GameWorld.player |-> Player.name

      def position: Lens[GameWorld, Position] =
        GameWorld.player |-> Player.position

      def x: Lens[GameWorld, Int] =
        GameWorld.player |-> Player.position |-> Position.x

      def y: Lens[GameWorld, Int] =
        GameWorld.player |-> Player.position |-> Position.y

      def grid: Lens[GameWorld, Vector[Vector[String]]] =
        GameWorld.field |-> Field.grid

      def getLine(): IO[String] =
        IO(readLine())

      def putLine(s: String): IO[Unit] =
        IO(println(s))
    }

    import Logic._
    def safeRun(): IO[Unit] =
      initWorld()
        .flatMap(world => IO(gameLoop(world)))

    def run(): Unit =
      safeRun()
        .unsafeRun()
  }
}
