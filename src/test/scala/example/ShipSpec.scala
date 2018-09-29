package battleship
import org.scalatest.FunSuite

class ShipSpec extends FunSuite {
    test("constructor: if we do not provide lifePoints, it should be equals to the size parameter.") {
        assert(new Ship("ship", 5).lifePoints == 5)
    }

    test("isSunk(): should return true if lifePoints == 0") {
        assert(new Ship("ship", 5, 0).isSunk)
    }

    test("hit(): should return a new Ship with lifePoints decremented by one.") {
        val initialShip = new Ship("ship", 5)
        val newShip = initialShip.hit
        assert(newShip.lifePoints == 4)
        assert(newShip ne initialShip)
    }

    test("hit(): called n times on a Ship with n lifePoints should sink it.") {
        val initialShip = new Ship("ship", 3)
        val newShip = initialShip.hit.hit.hit
        assert(newShip.isSunk) 
    }
}
