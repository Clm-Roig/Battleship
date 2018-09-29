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

    // ====== addShip() tests
    // x and y
    test("addShip(): if x or y are not between 0 and 9, should throw a InvalidCoordinateException.") { f =>
        val (x,y,dir) = (-3, 5, "N")
        assertThrows[InvalidCoordinateException] {
            f.grid.addShip(x, y, f.ship, dir)
        }
        val (x2,y2) = (3, 50)
        assertThrows[InvalidCoordinateException] {
            f.grid.addShip(x2, y2, f.ship, dir)
        }
    } 

    test("addShip(): x and y must be between 0 and 9.") { f =>
        val (x,y,dir) = (4, 6, "N")
        val newGrid = f.grid.addShip(x, y, f.ship, dir)
        assert(newGrid match {
            case g: Grid => true
            case _ => false
        })
    } 

    // direction
    test("addShip(): direction must be S, N, W or E. If not, should throw a InvalidDirectionException.") { f =>
        val (x,y,dir) = (3, 5, "X")
        assertThrows[InvalidDirectionException] {
            f.grid.addShip(x, y, f.ship, dir)
        }
    }

    test("addShip(): direction must be S, N, W or E.") { f =>
        val (x,y,dir) = (3, 5, "S")
        val newGrid = f.grid.addShip(x, y, f.ship, dir)
        assert(newGrid match {
            case g: Grid => true
            case _ => false
        })
    }
    
    // ===== nextCell() tests
    test("nextCell(): common usage.") { f => 
        val nextCell = f.grid.nextCell(5,5,"N")
        assert(nextCell._1 == 5)
        assert(nextCell._2 == 4)

        val nextCell2 = f.grid.nextCell(1,3,"E")
        assert(nextCell2._1 == 2)
        assert(nextCell2._2 == 3)
    }

    test("nextCell(): out of grid result.") { f => 
        val nextCell = f.grid.nextCell(0,0,"W")
        assert(nextCell._1 == -1)
        assert(nextCell._2 == 0)
    }  
}
