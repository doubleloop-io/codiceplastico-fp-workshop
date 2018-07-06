package day1.solutions

import scala.io.StdIn._

object Round1 {
  class Game {
    import Domain._

    object Domain {

      case class Player(name: String, var x: Int, var y: Int) {
        def move(delta: (Int, Int)): Unit = {
          x += delta._1
          y += delta._2
        }
      }

      object Player {
        def begin(name: String) = Player(name, 0, 0)
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

      case class GameWorld(player: Player, field: Field)
    }

    object Logic {
      import Domain._

      val enter = System.getProperty("line.separator")

      var world: GameWorld = null

      def initWorld(): Unit = {
        world = GameWorld(Player.begin(askName()), Field.mk3x3)
        println("Use commands to play")
      }

      def askName(): String = {
        println("What is your name?")
        val name = readLine().trim
        println(s"Hello, $name, welcome to the game!")
        name
      }

      def gameLoop(): Unit = {
        if (gameStep())
          gameLoop()
      }

      def gameStep(): Boolean = {
        val line = readLine()

        if (line.length > 0) {
          val words = line.trim.toLowerCase.split("\\s+")
          words(0) match {

            case "help" => {
              printHelp()
              true
            }

            case "show" => {
              printWorld()
              true
            }

            case "move" => {
              if (words.length < 2)
                println("Missing direction")
              else {
                words(1) match {
                  case "up"    => world.player.move((-1, 0))
                  case "down"  => world.player.move((1, 0))
                  case "right" => world.player.move((0, 1))
                  case "left"  => world.player.move((0, -1))
                  case _       => println("Unknown direction")
                }
              }
              true
            }

            case "quit" => {
              printQuit()
              false
            }

            case _ => {
              println("Unknown command")
              true
            }

          }
        } else
          true
      }

      def printWorld(): Unit =
        println(render)

      def printQuit(): Unit =
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

      def render: String = {
        val x       = world.player.x
        val y       = world.player.y
        val grid    = world.field.grid
        val updated = grid.updated(x, grid(x).updated(y, "x"))

        enter + updated.map(_.mkString(" | ")).mkString(enter) + enter
      }
    }

    import Logic._
    def run(): Unit = {
      initWorld()
      gameLoop()
    }
  }
}
