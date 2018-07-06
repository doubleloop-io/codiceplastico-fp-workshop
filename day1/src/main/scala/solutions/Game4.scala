package day1.solutions

import scala.io.StdIn._

object Game4 {
  class Game {
    import Domain._

    object Domain {

      case class Position(x: Int, y: Int) {
        def shift(delta: Position): Position = {
          val newx = x + delta.x
          val newy = y + delta.y
          copy(x = newx, y = newy)
        }
      }

      object Position {
        val origin = Position(0, 0)
      }

      case class Player(name: String, position: Position) {
        def move(delta: Position): Player =
          copy(position = position.shift(delta))
      }

      object Player {
        def begin(name: String) = Player(name, Position.origin)
      }

      case class Field(grid: Vector[Vector[String]])

      object Field {
        def mk3x3 = Field(
          Vector(
            Vector("-", "-", "-"),
            Vector("-", "-", "-"),
            Vector("-", "-", "-")
          )
        )
      }

      case class GameWorld(player: Player, field: Field) {}
    }

    object Logic {
      import Domain._

      val enter = System.getProperty("line.separator")

      def initWorld(): GameWorld = {
        val world = GameWorld(Player.begin(askName()), Field.mk3x3)
        println("Use commands to play")
        world
      }

      def askName(): String = {
        println("What is your name?")
        val name = readLine().trim
        println(s"Hello, $name, welcome to the game!")
        name
      }

      def gameLoop(world: GameWorld): Unit = {
        gameStep(world) match {
          case Some(w) => gameLoop(w)
          case None    => ()
        }
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
                words(1) match {
                  case "up"    => world.copy(player = world.player.move(Position(-1, 0)))
                  case "down"  => world.copy(player = world.player.move(Position(1, 0)))
                  case "right" => world.copy(player = world.player.move(Position(0, 1)))
                  case "left"  => world.copy(player = world.player.move(Position(0, -1)))
                  case _ => {
                    println("Unknown direction")
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

      def printWorld(world: GameWorld): Unit =
        println(render(world))

      def printQuit(world: GameWorld): Unit =
        println(s"Bye bye ${world.player.name}!")

      def printHelp(): Unit = {
        val expected =
          s"""|
         |Valid commands:
         |
         | help
         | show
         | move <up|down|left|right>
         | quit
         |""".stripMargin
        println(expected)
      }

      def render(world: GameWorld): String = {
        val (x, y)  = Position.unapply(world.player.position).get
        val grid    = world.field.grid
        val updated = grid.updated(x, grid(x).updated(y, "x"))

        enter + updated.map(_.mkString(" | ")).mkString(enter) + enter
      }

      def end: Option[GameWorld]                        = None
      def continue(world: GameWorld): Option[GameWorld] = Some(world)
    }

    import Logic._
    def run(): Unit = {
      val world = initWorld()
      gameLoop(world)
    }
  }
}
