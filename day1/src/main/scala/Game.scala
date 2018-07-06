package day1

import scala.io.StdIn._

class Game {
  import Domain._

  object Domain {

    case class Position(x: Int, y: Int)

    object Position {
      val start = Position(0, 0)
    }

    case class Player(name: String, position: Position)

    object Player {
      def begin(name: String) = Player(name, Position.start)
    }

    case class Land(grid: Vector[Vector[Unit]])

    object Land {
      def mk3x3 = Land(
        Vector(
          Vector((), (), ()),
          Vector((), (), ()),
          Vector((), (), ())
        )
      )
    }

    case class GameWorld(player: Player, land: Land) {}
  }

  object Logic {
    import Domain._

    var executing        = true
    var world: GameWorld = null

    def initWorld(): Unit =
      world = GameWorld(Player.begin(askName()), Land.mk3x3)

    def gameLoop(): Unit =
      while (executing) {
        executing = gameStep()
      }

    def gameStep(): Boolean = {
      println("Enter a command:")
      val line = readLine()

      if (line.length == 0)
        true
      else {
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
            ???
            true
          }

          case "quit" => {
            printQuit()
            false
          }

        }
      }
    }

    def askName(): String = {
      println("What is your name?")
      val name = readLine().trim
      println(s"Hello, $name, welcome to the game!")
      name
    }

    def printWorld(): Unit =
      println(render)

    def printQuit(): Unit =
      println(s"Bye bye ${world.player.name}!")

    def printHelp(): Unit = {
      val expected =
        s"""Valid commands:
         |
         | help
         | show
         | move <up|down|left|right>
         | quit
         |""".stripMargin
      println(expected)
    }

    def render: String = {

      def renderField =
        world.land.grid.map(_.map(_ => "-"))

      def renderPlayer(field: Vector[Vector[String]]) = {
        val row = field(world.player.position.x)
        field.updated(world.player.position.x, row.updated(world.player.position.y, "x"))
      }

      val enter = System.getProperty("line.separator")
      val field = renderPlayer(renderField)
      enter + field.map(_.mkString(" | ")).mkString(enter) + enter
    }
  }

  import Logic._
  def run(): Unit = {
    initWorld()
    gameLoop()
  }
}
