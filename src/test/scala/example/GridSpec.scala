package battleship
import org.scalatest.fixture

class GridSpec extends fixture.FunSuite {

    // Fixture types
    case class FixtureParam(ship: Ship, grid: Grid)

    def withFixture(test: OneArgTest): org.scalatest.Outcome = {
        val ship = new Ship("testedShip", 4)
        val grid = new Grid()
        try test(FixtureParam(ship, grid))
        finally {
            // perform some cleanup...
        }
    }

    test("addShip(): if x or y are not between 0 and 9, should return None") { f =>
        val (x,y) = (-3, 5)
        assert(f.grid.addShip(x, y, f.ship, "N") == None)
    } 
}
