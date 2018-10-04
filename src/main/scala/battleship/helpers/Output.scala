package battleship
import Console.{WHITE, RED, CYAN, UNDERLINED}

trait Output {
    def display(msg: String): Unit
    def displayError(msg: String): Unit
    def clear(): Unit
}

// Console Output object (standard output). Uses colors.
object ConsoleOutput extends Output {
    override def display(msg: String): Unit = println(msg) 
    override def displayError(msg: String): Unit = println(s"$RED"+msg+s"$WHITE")
    override def clear(): Unit = print("\033[H\033[2J")
}

// A mock console which does nothing.
object MockConsoleOutput extends Output {
    override def display(msg: String): Unit = {}
    override def displayError(msg: String): Unit = {}
    override def clear(): Unit = {}
}
