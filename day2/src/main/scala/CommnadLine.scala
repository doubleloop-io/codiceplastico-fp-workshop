package day2

object CommandLine {

  object Round1 {

    sealed trait Command
    final case class Echo(value: String) extends Command
    final case class Sort(value: String) extends Command
    final case object Help extends Command

    def parse(cmdName: String, arg: String): Command =
      cmdName match {
        case "echo" => Echo(arg)
        case "sort" => Sort(arg)
        case "help" => Help
      }

    def exec(cmd: Command): String =
      cmd match {
        case Echo(v) => v
        case Sort(v) => v.split(' ').sorted.mkString(" ")
        case Help    => "Try typing an actual command, dummy!"
      }

    def demo(line: String) = {
      val a = line.split(' ')
      val c = parse(a.head, a.tail.mkString(" "))
      val r = exec(c)
      println(r)
    }

  }

  object Round2 {

    case class Configuration(cmds: List[Command])
    case class Command(name: String, run: String => Invocation)
    case class Invocation(of: Command, args: String, exec: String)

    def parse(
        cmds: List[Command],
        name: String,
        args: String
    ): Option[Invocation] =
      cmds
        .find(x => x.name == name)
        .map(x => x.run(args))

    def process(conf: Configuration, input: String): String = {
      val a = input.split(' ')
      val r = parse(conf.cmds, a.head, a.tail.mkString(" "))
      r match {
        case Some(i) => s"Ran ${i.of.name} with args ${i.args}: ${i.exec}"
        case None    => "Unrecognised command."
      }
    }

    val echoCommand: Command =
      Command("echo", x => Invocation(echoCommand, x, x))
    val sortCommand: Command = Command(
      "sort",
      x => Invocation(sortCommand, x, x.split(' ').sorted.mkString(" "))
    )
    val helpCommand: Command =
      Command("help", x => Invocation(helpCommand, x, "Come on, it's easy!"))
    val commands = List(echoCommand, sortCommand, helpCommand)

    def demo(line: String) = {
      println(process(Configuration(commands), line))
    }
  }
}
