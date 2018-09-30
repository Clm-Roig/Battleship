package battleship

trait Output {
    /**
        Display a message given.
    */
    def display(msg: String): Unit
}

object ConsoleOutput extends Output {
    override def display(msg: String): Unit = {
        println(msg)
    }
}