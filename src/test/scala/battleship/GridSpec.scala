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
    test("addShip(): if x is not between 0 and 9 or y between A and J, should return None.") { f =>
        val (x,y,dir) = (-3, 'D', "N")
        assert(!f.grid.addShip(x, y, f.ship, dir).isDefined)
        val (x2,y2) = (3, '5')
        assert(!f.grid.addShip(x2, y2, f.ship, dir).isDefined)
        val (x3,y3) = (3, 'K')
        assert(!f.grid.addShip(x3, y3, f.ship, dir).isDefined)
    } 

    test("addShip(): common usage.") { f =>
        val (x,y,dir) = (4, 'E', "N")
        val newGrid = f.grid.addShip(x, y, f.ship, dir)
        assert(newGrid.isDefined)
        assert(newGrid.get.ships.length == 1)
    } 

    // direction
    test("addShip(): direction must be S, N, W or E. If not, should return None.") { f =>
        val (x,y,dir) = (3, 'D', "X")
        val newGrid = f.grid.addShip(x, y, f.ship, dir)
        assert(newGrid.isDefined)
    }

    test("addShip(): direction must be S, N, W or E.") { f =>
        val (x,y,dir) = (3, 'D', "S")
        val newGrid = f.grid.addShip(x, y, f.ship, dir)
        assert(newGrid.isDefined)
    }

    // Overlaps
    test("addShip(): should return None if try to place a ship over another one.") { f =>
        val (x,y,dir) = (9, 'A', "N")
        val newGrid = f.grid.addShip(x,y,f.ship,dir).get
        val newGrid2 = newGrid.addShip(x,y,f.ship,dir)
        assert(!newGrid2.isDefined)
    }

    // Out of grid
    test("addShip(): should return None if try to place a ship out of the Grid.") { f =>
        val (x,y,dir) = (9, 'A', "S")
        val newGrid = f.grid.addShip(x,y,f.ship,dir)
        assert(!newGrid.isDefined)
    }

    // ===== areAllShipsSunk() tests
    test("areAllShipsSunk(): common usage.") { f => 
        assert(f.grid.areAllShipsSunk())
        val newGrid = f.grid.addShip(1,'B',f.ship,"S").get
        assert(!newGrid.areAllShipsSunk())
        val finalGrid = newGrid.shootHere(1,1)._3.shootHere(2,1)._3.shootHere(3,1)._3.shootHere(4,1)._3
        assert(finalGrid.areAllShipsSunk())
    }

    // ===== getCellsToCheck() tests
    test("getCellsToCheck(): common usage.") { f => 
        val cells = f.grid.getCellsToCheck(0,0,"S",2)
        assert(cells.sameElements(Array((0,0),(1,0))))
        
        val cells2 = f.grid.getCellsToCheck(1,1,"E",3)        
        assert(cells2.sameElements(Array((1,1),(1,2),(1,3))))    
    }

    // ===== isShipHere() tests
    test("isShipHere(): common usage.") { f => 
        val newGrid = f.grid.addShip(1,'B',f.ship,"S")
        assert(newGrid.get.isShipHere(4,1))
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

    // ===== shootHere() tests
    test("shootHere(): ship hit.") { f => 
        val initialGrid = f.grid.addShip(3,'B',f.ship,"S").get
        val resultAfterShoot = initialGrid.shootHere(3,1)

        val ship: Option[Ship] = resultAfterShoot._1
        val shootState: String = resultAfterShoot._2
        val newGrid: Grid = resultAfterShoot._3

        assert(shootState == "hit")
        assert(ship.get.symbol == "T")
        assert(newGrid.positions(3)(1) == "T_hit")
    } 

    test("shootHere(): ship sunk.") { f => 
        val initialGrid = f.grid.addShip(1,'B',new Ship("Test", "T", 2),"S").get
        val resultAfterShoot = initialGrid.shootHere(1,1)
        val newGrid: Grid = resultAfterShoot._3

        val resultAfterShoot2 = newGrid.shootHere(2,1)
        val ship2: Option[Ship] = resultAfterShoot2._1
        val shootState2: String = resultAfterShoot2._2
        val newGrid2: Grid = resultAfterShoot2._3

        assert(shootState2 == "sunk")
        assert(ship2.get.symbol == "T")
        assert(newGrid2.positions(2)(1) == "T_hit")
    } 

    test("shootHere(): miss.") { f => 
        val resultAfterShoot = f.grid.shootHere(2,1)

        val ship: Option[Ship] = resultAfterShoot._1
        val shootState: String = resultAfterShoot._2
        val newGrid: Grid = resultAfterShoot._3

        assert(shootState == "miss")
        assert(!ship.isDefined)
        assert(newGrid.positions(2)(1) == "W_hit")
    }

    test("shootHere(): out of grid coordinate.") { f => 
        val resultAfterShoot = f.grid.shootHere(200,-100)

        val ship: Option[Ship] = resultAfterShoot._1
        val shootState: String = resultAfterShoot._2
        val newGrid: Grid = resultAfterShoot._3

        assert(shootState == "miss")
        assert(!ship.isDefined)
    }

    // ===== toStringToSelf() tests
    test("toStringToSelf(): common usage.") { f => 
        val newGrid = f.grid.addShip(1,'B',f.ship,"S").get
        val newGrid2 = newGrid.addShip(5,'E',new Ship("Destroyer", "D", 3), "E").get
        assert(newGrid2.toStringToSelf().contains("MY GRID"))
        assert(newGrid2.toStringToSelf().contains("_D_"))
    }
}
