package day1.solutions

import scala.io.StdIn._
import monocle.Lens
import monocle.macros.GenLens

object Round5 {
  class Game {
    import Domain._
    import Logic._

    object Domain {

      case class Player(name: String, x: Int, y: Int)

      object Player {
        object ExpandedStyle {
          val _name = Lens[Player, String](_.name)(v => s => s.copy(name = v))
          val _x    = Lens[Player, Int](_.x)(v => s => s.copy(x = v))
          val _y    = Lens[Player, Int](_.y)(v => s => s.copy(y = v))
        }

        val name = GenLens[Player](_.name)
        val x    = GenLens[Player](_.x)
        val y    = GenLens[Player](_.y)

        def begin(name: String) = Player(name, 0, 0)
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
                try {
                  words(1) match {
                    case "up"    => move(world, (-1, 0))
                    case "down"  => move(world, (1, 0))
                    case "right" => move(world, (0, 1))
                    case "left"  => move(world, (0, -1))
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

      def move(world: GameWorld, delta: (Int, Int)): GameWorld = {
        val newX = x.get(world) + delta._1
        val newY = y.get(world) + delta._2

        val size = grid.get(world).size - 1
        if (newX < 0
            || newY < 0
            || newX > size
            || newY > size) throw new Exception("Invalid direction")

        x.set(newX)
          .andThen(y.set(newY))(world)
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
        GameWorld.player.composeLens(Player.name)

      def x: Lens[GameWorld, Int] =
        GameWorld.player.composeLens(Player.x)

      def y: Lens[GameWorld, Int] =
        GameWorld.player.composeLens(Player.y)

      def grid: Lens[GameWorld, Vector[Vector[String]]] =
        GameWorld.field.composeLens(Field.grid)
    }

    def run(): Unit = {
      val world = initWorld()
      gameLoop(world)
    }
  }
}
