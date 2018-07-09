package day1.solutions

import scala.io.StdIn._

object Round6 {
  class Game {
    import day1.std._
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
                try {
                  words(1) match {
                    case "up"    => move(world, Upward())
                    case "down"  => move(world, Downward())
                    case "right" => move(world, Rightward())
                    case "left"  => move(world, Leftward())
                    case _ => {
                      println("Unknown direction")
                      world
                    }
                  }
                } catch {
                  case e: Exception => {
                    println(e.getMessage)
                    world
                  }
                }
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

      def move(world: GameWorld, direction: Direction): GameWorld = {
        val newX = x.get(world) + direction.delta.x
        val newY = y.get(world) + direction.delta.y

        val size = grid.get(world).size - 1
        if (newX < 0
            || newY < 0
            || newX > size
            || newY > size) throw new Exception("Invalid direction")

        x.set(newX, y.set(newY, world))
      }

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
        GameWorld.player |-> Player.name

      def position: Lens[GameWorld, Position] =
        GameWorld.player |-> Player.position

      def x: Lens[GameWorld, Int] =
        GameWorld.player |-> Player.position |-> Position.x

      def y: Lens[GameWorld, Int] =
        GameWorld.player |-> Player.position |-> Position.y

      def grid: Lens[GameWorld, Vector[Vector[String]]] =
        GameWorld.field |-> Field.grid
    }

    def run(): Unit = {
      val world = initWorld()
      gameLoop(world)
    }
  }
}
