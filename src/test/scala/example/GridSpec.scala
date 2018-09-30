package battleship
import org.scalatest.fixture

class GridSpec extends fixture.FunSuite {

    // Fixture types
    case class FixtureParam(ship: Ship, grid: Grid)

    def withFixture(test: OneArgTest): org.scalatest.Outcome = {
        val ship = new Ship("testedShip","T",4)
        val grid = new Grid()
        try test(FixtureParam(ship, grid))
        finally {
            // perform some cleanup...
        }
    }

    // ====== addShip() tests
    // x and y
    test("addShip(): if x is not between 0 and 9 or y between A and J, should throw a InvalidCoordinateException.") { f =>
        val (x,y,dir) = (-3, 'D', "N")
        assertThrows[InvalidCoordinateException] {
            f.grid.addShip(x, y, f.ship, dir)
        }
        val (x2,y2) = (3, '5')
        assertThrows[InvalidCoordinateException] {
            f.grid.addShip(x2, y2, f.ship, dir)
        }

        val (x3,y3) = (3, 'K')
        assertThrows[InvalidCoordinateException] {
            f.grid.addShip(x3, y3, f.ship, dir)
        }
    } 

    test("addShip(): common usage.") { f =>
        val (x,y,dir) = (4, 'E', "N")
        val newGrid = f.grid.addShip(x, y, f.ship, dir)
        assert(newGrid match {
            case g: Grid => true
            case _ => false
        })
    } 

    // direction
    test("addShip(): direction must be S, N, W or E. If not, should throw a InvalidDirectionException.") { f =>
        val (x,y,dir) = (3, 'D', "X")
        assertThrows[InvalidDirectionException] {
            f.grid.addShip(x, y, f.ship, dir)
        }
    }

    test("addShip(): direction must be S, N, W or E.") { f =>
        val (x,y,dir) = (3, 'D', "S")
        val newGrid = f.grid.addShip(x, y, f.ship, dir)
        assert(newGrid match {
            case g: Grid => true
            case _ => false
        })
    }

    // ===== getCellsToCheck() tests
    test("getCellsToCheck(): common usage.") { f => 
        val cells = f.grid.getCellsToCheck(0,0,"S",2)
        assert(cells.sameElements(Array((0,0),(1,0),(2,0))))
        
        val cells2 = f.grid.getCellsToCheck(1,1,"E",3)        
        assert(cells2.sameElements(Array((1,1),(1,2),(1,3),(1,4))))    
    }

    // ===== isShipHere() tests
    test("isShipHere(): common usage.") { f => 
        val newGrid = f.grid.addShip(1,'B',f.ship,"S")
        assert(newGrid.isShipHere(4,1))
    }
    
    // ===== nextCell() tests
    test("nextCell(): common usage.") { f => 
        val nextCell = f.grid.nextCell(5,5,"N")
        assert(nextCell._1 == 4)
        assert(nextCell._2 == 5)

        val nextCell2 = f.grid.nextCell(1,3,"E")
        assert(nextCell2._1 == 1)
        assert(nextCell2._2 == 4)
    }

    test("nextCell(): out of grid result.") { f => 
        val nextCell = f.grid.nextCell(0,0,"W")
        assert(nextCell._1 == 0)
        assert(nextCell._2 == -1)
    }  

    // ===== toStringToSelf() tests
    test("toStringToSelf()") { f => 
        val newGrid = f.grid.addShip(1,'B',f.ship,"S")
        val newGrid2 = newGrid.addShip(5,'E',new Ship("Destroyer", "D_hit", 3), "E")
        assert(newGrid2.toStringToSelf().contains("MY GRID"))
    }
}
