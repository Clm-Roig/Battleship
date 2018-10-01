package battleship
import org.scalatest.FunSuite

class HumanSpec extends FunSuite {
    // ====== askToEnterXCoordinate() tests
    test("askToEnterXCoordinate(): common usage.") {
        val inputString = new MockConsoleInput(List("5"))
        val human = Human("Kévin", input = inputString)   
        assert(human.askToEnterXCoordinate() == 5)
    }

    test("askToEnterXCoordinate(): x too big or to small.") {
        // askToEnterXCoordinate() will be call 3 times because 23 and -1 are not valid values.
        // Finally, it will return 3 which is a valid value
        val inputString = new MockConsoleInput(List("23", "-1", "3"))
        val human = Human("Kévin", input = inputString)   
        assert(human.askToEnterXCoordinate() == 3)
    }

    // ====== askToEnterYCoordinate() tests
    test("askToEnterYCoordinate(): common usage (type 'a' or 'A' must return 'A').") {
        val inputString = new MockConsoleInput(List("A", "a"))
        val human = Human("Kévin", input = inputString)   
        assert(human.askToEnterYCoordinate() == 'A')
        assert(human.askToEnterYCoordinate() == 'A')
    }

    test("askToEnterYCoordinate(): y invalid (integer or char too high).") {
        // askToEnterYCoordinate() will be call 3 times because 23 and z are not valid values.
        // Finally, it will return C which is a valid value
        val inputString = new MockConsoleInput(List("23", "z", "C"))
        val human = Human("Kévin", input = inputString)   
        assert(human.askToEnterYCoordinate() == 'C')
    }
}