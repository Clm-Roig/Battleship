package battleship

trait Input {
    def askString(): String
}

// Standard input object
object ConsoleInput extends Input {
    override def askString(): String = {
        scala.io.StdIn.readLine()    
    }
}

// Mock input object used for tests (TODO: self updating, breaks RT!!!)
case class MockConsoleInput(var msg: List[String]) extends Input {
    override def askString(): String = {
        val res = this.msg.head
        this.msg = this.msg.tail
        res
    }
}