package battleship
import org.scalatest.FunSuite

class MockConsoleInputSpec extends FunSuite {
    // TODO: askString() is not RT...
    test("askString(): must return successively the Strings entered.") {
        val in = new MockConsoleInput(List("test","1","ok"))
        assert(in.askString() == "test")
        assert(in.askString() == "1")
        assert(in.askString() == "ok")
    }
}