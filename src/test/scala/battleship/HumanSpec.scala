package battleship
import org.scalatest.FunSuite

class HumanSpec extends FunSuite {

    // ===== askToPlaceAShip() tests
    test("askToPlaceAShip(): common usage.") {
        val inputString = new MockConsoleInput(List("5", "a", "E"))
        val human = new Human("Kévin", input = Some(inputString), output = Some(MockConsoleOutput)) 
        val newHuman = human.askToPlaceAShip(new Ship("Destroyer", "D", 2))
        assert(newHuman match {
            case g: Human => true
            case _ => false
        })
        assert(newHuman.myGrid.positions(5)(0) == "D")
        assert(newHuman.myGrid.positions(5)(1) == "D")
    }

    // ===== askForShootCoordinates() tests
    test("askForShootCoordinates(): common usage.") {
        val inputString = new MockConsoleInput(List("5", "A"))
        val human = new Human("Kévin", input = Some(inputString), output = Some(MockConsoleOutput)) 
        val tuple = human.askForShootCoordinates(new Grid())
        assert(tuple._1 == 5)
        assert(tuple._2 == 0)
    }

    test("askForShootCoordinates(): incorrect x and y.") {
        val inputString = new MockConsoleInput(List("23","4","z","C"))
        val human = new Human("Kévin", input = Some(inputString), output = Some(MockConsoleOutput)) 
        val tuple = human.askForShootCoordinates(new Grid())
        assert(tuple._1 == 4)
        assert(tuple._2 == 2)
    }

    // ===== askToEnterXCoordinate() tests
    test("askToEnterXCoordinate(): common usage.") {
        val inputString = new MockConsoleInput(List("5"))
        val human = new Human("Kévin", input = Some(inputString), output = Some(MockConsoleOutput))   
        assert(human.askToEnterXCoordinate() == 5)
    }

    test("askToEnterXCoordinate(): invalid x (char).") {
        // askToEnterXCoordinate() will be call 2 times because z is not a valid value.
        // Finally, it will return 3 which is a valid value
        val inputString = new MockConsoleInput(List("z", "3"))
        val human = new Human("Kévin", input = Some(inputString), output = Some(MockConsoleOutput))   
        assert(human.askToEnterXCoordinate() == 3)
    }

    test("askToEnterXCoordinate(): invalid x (too big or to small).") {
        // askToEnterXCoordinate() will be call 3 times because 23 and -1 are not valid values.
        // Finally, it will return 3 which is a valid value
        val inputString = new MockConsoleInput(List("23", "-1", "3"))
        val human = new Human("Kévin", input = Some(inputString), output = Some(MockConsoleOutput))   
        assert(human.askToEnterXCoordinate() == 3)
    }

    // ===== askToEnterYCoordinate() tests
    test("askToEnterYCoordinate(): common usage (type 'a' or 'A' must return 'A').") {
        val inputString = new MockConsoleInput(List("A", "a"))
        val human = new Human("Kévin", input = Some(inputString), output = Some(MockConsoleOutput))   
        assert(human.askToEnterYCoordinate() == 'A')
        assert(human.askToEnterYCoordinate() == 'A')
    }

    test("askToEnterYCoordinate(): y invalid (integer or char too high).") {
        // askToEnterYCoordinate() will be call 3 times because 23 and z are not valid values.
        // Finally, it will return C which is a valid value
        val inputString = new MockConsoleInput(List("23", "z", "C"))
        val human = new Human("Kévin", input = Some(inputString), output = Some(MockConsoleOutput))   
        assert(human.askToEnterYCoordinate() == 'C')
    }

    // ===== askToEnterDirection() tests
    test("askToEnterDirection(): common usage.") {
        val inputString = new MockConsoleInput(List("E"))
        val human = new Human("Kévin", input = Some(inputString), output = Some(MockConsoleOutput))   
        assert(human.askToEnterDirection() == "E")
    }

    test("askToEnterDirection(): invalid direction.") {
        val inputString = new MockConsoleInput(List("X", "N"))
        val human = new Human("Kévin", input = Some(inputString), output = Some(MockConsoleOutput))   
        assert(human.askToEnterDirection() == "N")
    }
}