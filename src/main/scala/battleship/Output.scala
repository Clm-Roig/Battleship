package battleship
import Console.{WHITE, RED, CYAN, UNDERLINED}

trait Output {
    /**
        Display a message given.
    */
    def display(msg: String): Unit
    def displayError(msg: String): Unit
    def clear(): Unit
}

// Console Output object (standard output)
object ConsoleOutput extends Output {
    override def display(msg: String): Unit = {
        println(msg)
    }

    override def displayError(msg: String): Unit = {
        println(s"$RED"+msg+s"$WHITE")
    }

    override def clear(): Unit = {
        print("\033[H\033[2J");
    }
}

object MockConsoleOutput extends Output {
     override def display(msg: String): Unit = {
        // Do nothing
    }

    override def displayError(msg: String): Unit = {
        // Do nothing
    }

    override def clear(): Unit = {
        // Do nothing
    }
}
