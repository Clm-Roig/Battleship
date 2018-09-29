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
    test("addShip(): if x or y are not between 0 and 9, should throw a CoordinateException.") { f =>
        val (x,y, dir) = (-3, 5, "N")
        assertThrows[CoordinateException] {
            f.grid.addShip(x, y, f.ship, dir)
        }
        val (x2,y2) = (3, 50)
        assertThrows[CoordinateException] {
            f.grid.addShip(x2, y2, f.ship, dir)
        }
    } 

    test("addShip(): x and y must be between 0 and 9.") { f =>
        val (x,y, dir) = (4, 6, "N")
        assert(!f.grid.addShip(x, y, f.ship, dir).isEmpty)
    } 

    // direction
    test("addShip(): direction must be S, N, W or E. If not, should throw a InvalidDirectionException.") { f =>
        val (x,y, dir) = (3, 5, "X")
        assertThrows[InvalidDirectionException] {
            f.grid.addShip(x, y, f.ship, dir)
        }
    }

    test("addShip(): direction must be S, N, W or E.") { f =>
        val (x,y, dir) = (3, 5, "S")
        assert(!f.grid.addShip(x, y, f.ship, dir).isEmpty)
    }
}
