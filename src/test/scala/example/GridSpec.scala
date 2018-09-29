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

    // x and y
    test("addShip(): if x or y are not between 0 and 9, should return None.") { f =>
        val (x,y, dir) = (-3, 5, "N")
        assert(f.grid.addShip(x, y, f.ship, dir).isEmpty)
    } 

    test("addShip(): x and y must be between 0 and 9.") { f =>
        val (x,y, dir) = (4, 6, "N")
        assert(!f.grid.addShip(x, y, f.ship, dir).isEmpty)
    } 

    // direction
    test("addShip(): direction must be S, N, W or E. If not, should return None.") { f =>
        val (x,y, dir) = (3, 5, "X")
        assert(f.grid.addShip(x, y, f.ship, dir).isEmpty)
    }

    test("addShip(): direction must be S, N, W or E.") { f =>
        val (x,y, dir) = (3, 5, "S")
        assert(!f.grid.addShip(x, y, f.ship, dir).isEmpty)
    }
}
