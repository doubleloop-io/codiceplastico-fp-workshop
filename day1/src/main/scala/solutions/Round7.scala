package day1.solutions

import scala.io.StdIn._
import cats.implicits._
import monocle.Lens
import monocle.macros.GenLens

object Round7 {
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
      import Domain._

      val enter = System.getProperty("line.separator")

      def initWorld(): GameWorld = {
        val world = GameWorld(Player.begin(askName()), Field.mk20x20)
        println("Use commands to play")
        world
      }

      def askName(): String = {
        println("What is your name?")
        val name = readLine().trim
        println(s"Hello, $name, welcome to the game!")
        name
      }

      def gameLoop(world: GameWorld): Unit =
        gameStep(world) match {
          case Some(w) => gameLoop(w)
          case None    => ()
        }

      def gameStep(world: GameWorld): Option[GameWorld] = {
        val line = readLine()

        if (line.length > 0) {
          val words = line.trim.toLowerCase.split("\\s+")
          words(0) match {

            case "help" => {
              printHelp()
              continue(world)
            }

            case "show" => {
              printWorld(world)
              continue(world)
            }

            case "move" => {
              val newWorld = if (words.length < 2) {
                println("Missing direction")
                world
              } else {
                val e = words(1) match {
                  case "up"    => move(world, Upward())
                  case "down"  => move(world, Downward())
                  case "right" => move(world, Rightward())
                  case "left"  => move(world, Leftward())
                  case _       => Left("Unknown direction")
                }
                e.fold(m => {
                  println(m)
                  world
                }, identity)
              }
              continue(newWorld)
            }

            case "quit" => {
              printQuit(world)
              end
            }

            case _ => {
              println("Unknown command")
              continue(world)
            }

          }
        } else
          continue(world)
      }

      def move(world: GameWorld, direction: Direction): Either[String, GameWorld] =
        position
          .modifyF(newPosition(world, direction.delta, _))(world)
          .toRight("Invalid direction")

      def newPosition(world: GameWorld, delta: Delta, current: Position): Option[Position] = {
        val next = Position(current.x + delta.x, current.y + delta.y)
        cell(world, next).map(_ => next)
      }

      def cell(world: GameWorld, position: Position): Option[String] =
        grid
          .get(world)
          .lift(position.x)
          .flatMap(_.lift(position.y))

      def printWorld(world: GameWorld): Unit =
        println(renderWorld(world))

      def printQuit(world: GameWorld): Unit =
        println(s"Bye bye ${name.get(world)}!")

      def printHelp(): Unit = {
        val value =
          s"""|
         |Valid commands:
         |
         | help
         | show
         | move <up|down|left|right>
         | quit
         |""".stripMargin
        println(value)
      }

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
        GameWorld.player.composeLens(Player.name)

      def position: Lens[GameWorld, Position] =
        GameWorld.player.composeLens(Player.position)

      def x: Lens[GameWorld, Int] =
        GameWorld.player.composeLens(Player.position).composeLens(Position.x)

      def y: Lens[GameWorld, Int] =
        GameWorld.player.composeLens(Player.position).composeLens(Position.y)

      def grid: Lens[GameWorld, Vector[Vector[String]]] =
        GameWorld.field.composeLens(Field.grid)
    }

    def run(): Unit = {
      val world = initWorld()
      gameLoop(world)
    }
  }
}
