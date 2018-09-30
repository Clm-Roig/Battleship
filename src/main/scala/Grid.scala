package battleship
import Console.{WHITE_B, BLACK, WHITE, RED, RESET, CYAN, UNDERLINED}

/**
    The square grid used for the Battleship, where the players put their ships. 
    At initialization, the grid is filled with water ("W" char).
    | 
   -|-------------- y ---------------->
    |       0   1   2   3   4   5 ...
    |      _______________________
    |   0 |_W_|_W_|_W_|_W_|_W_|_W_|         N
    x   1 |_W_|_W_|_W_|_W_|_W_|_W_|         Λ
    |   2 |_W_|_W_|_W_|_W_|_W_|_W_|    W <--|--> E
    |   3 |_W_|_W_|_W_|_W_|_W_|_W_|         V
    |   ...                                 S
    V

    @param ships ships on the grid
    @param size size of the grid (default: 10)
    @param positions 2 dimensions array representing 
        the cells of the grid, each cell can be water or ship (hit or not for both)
    @param output output class used to display messages
 */
case class Grid (ships: Array[Ship], size: Int, positions: Array[Array[String]], output: Output = ConsoleOutput) {

    // ===== Constructors
    def this(size: Int) = {
        this(Array(), size, Array.ofDim[String](size, size).map(x => x.map(x => Grid.WATER)))
    }

    def this() = this(Grid.DEFAULT_SIZE)

    // ===== Methods
    /**
    Add a ship to the grid. The ship will be placed at (x,y) and facing the direction given.
    @param x x coordinate, equivalent to A,B,C,...,J converted in Int
    @param y y coordinate
    @param s Ship to place on the Grid 
    @param direction direction of the ship

    @throws InvalidCoordinateException if x or y are not between 0 and Grid size
    @throws InvalidDirectionException if direction is not part of VALID_DIRECTIONS
    @return Option[Grid], Some if the Ship was correctly added, else None.
    */
    def addShip(x: Int, yChar: Char, s: Ship, direction: String): Grid = {
        // Convert y to Int (index in alphabet)
        val y = yChar.toUpper.toInt - 'A'.toInt

        // Tests data provided
        if(!yChar.isLetter) throw new InvalidCoordinateException("y must be a letter between A and " + (this.size + 'A' - 1).toChar + ".")
        if(y > this.size-1 || y < 0) throw new InvalidCoordinateException("y must be a letter between A and " + (this.size + 'A' - 1).toChar + ".")
        if(x > this.size-1 || x < 0) throw new InvalidCoordinateException("x must be between 0 and " + (this.size - 1) + ".")
        if(!Grid.VALID_DIRECTIONS.contains(direction)) 
            throw new InvalidDirectionException("direction must be a value in [\"" + (Grid.VALID_DIRECTIONS mkString "\", \"") + "\"].")

        // Attempt to place the Ship
        val cellsToCheck = (x,y) +: this.getCellsToCheck(x,y,direction,s.size)

        // Test if it overlaps a Ship
        cellsToCheck.foreach(tuple => {
            if (this.isShipHere(tuple._1, tuple._2))
                throw new ShipOverlapsException("The Ship you are trying to add is overlapping another one.")
        })

        // Test if it is out of grid
        cellsToCheck.foreach(tuple => {
            if(tuple._1 < 0||tuple._2 < 0||tuple._1 >= this.size||tuple._2 >= this.size) 
                throw new ShipOutOfGridException("The Ship you are trying to add is out of the grid.")
        })

        // Everything ok, place the Ship
        val newPositions = this.updatePositions(this.positions, cellsToCheck, s.symbol)
        
        this.copy(positions = newPositions)        
    }

    /**
        Return a new positions Array with all the cells given change to symbol.
        @param positions Array(Array[String]) current positions Array
        @param cellsToChange Array[(Int,Int)] cells to modify
        @param symbol String the symbol to use for modification

        @return Array(Array[String]) the new positions Array updated.
    */
    def updatePositions(positions: Array[Array[String]], cellsToChange: Array[(Int,Int)], symbol: String)
    : Array[Array[String]]  = {
        if(cellsToChange.length == 0) positions
        else {
            val cellToChange = cellsToChange.last
            var newPositions = Array.ofDim[String](this.size, this.size)
            positions.zipWithIndex.foreach{
                case(x,i) => {
                    if(i == cellToChange._1) {
                        x.zipWithIndex.foreach{
                            case(y, j) => {
                                if(j == cellToChange._2) {
                                    newPositions(i)(j) = symbol
                                }
                                else {
                                    newPositions(i)(j) = y
                                }
                            }
                        }
                    } else {
                        newPositions(i) = x
                    }
                }
            }
            val newCellsToChange = cellsToChange.take(cellsToChange.length - 1)
            updatePositions(newPositions, newCellsToChange, symbol)
        }
    }

    /**
        Return the cells passed through from the coordinate(x,y) according to the 
        direction and the nbOfCells given. The initial cell is included in the result.
        @param x x coordinate of the initial cell
        @param y y coordinate of the initial cell
        @param direction direction of the next cell, must be in VALID_DIRECTIONS
        @param nbOfCells number of cells to pass through
        @param previousCells cells passed through previously

        @return Array[(Int,Int)] the cells coordinates (x,y) passed through.
    */
    def getCellsToCheck(x: Int, y: Int, direction: String, nbOfCells: Int, 
    previousCells: Array[(Int, Int)] = Array()): Array[(Int, Int)] = {
        if(nbOfCells <= 0) previousCells :+ (x,y)
        else {
            val nextCell = this.nextCell(x,y,direction)
            val cells = previousCells :+ (x,y)
            getCellsToCheck(nextCell._1, nextCell._2, direction, nbOfCells - 1, cells)
        }            
    }

    /**
        Check if a Ship or Ship hit is at the coordinate(x,y).
        @param x x coordinate to check
        @param y y coordinate to check

        @return Boolean, true if there is a Ship or a Ship hit at the coordinate(x,y), else false
    */
    def isShipHere(x: Int, y: Int): Boolean = {
        try {
            this.positions(x)(y) != Grid.WATER && this.positions(x)(y) != Grid.WATER_HIT
        } catch {
            // Out of grid, there is no ship.
            case e: ArrayIndexOutOfBoundsException => false
        }
    }

    /**
        Return the coordinates of a next cell according to an initial cell and a direction.
        The next cell can be out of the grid.
        @param x initial cell x position
        @param y initial cell y position
        @param direction direction of the next cell (must be in VALID_DIRECTIONS)

        @return (Int, Int) the xCoord and yCoord of the next cell
    */
    def nextCell(x: Int, y: Int, direction: String): (Int,Int) = {
        val yCoord = direction match {
            case "W" => y - 1
            case "E" => y + 1
            case _ => y
        }
        val xCoord = direction match {
            case "N" => x - 1
            case "S" => x + 1
            case _ => x
        }
        (xCoord, yCoord)
    }

    /**
        String representation to be seen by the owner of the grid.
    */
    def toStringToSelf(): String = {
        var res = s"""$WHITE
        MY GRID:
          $WHITE_B$BLACK"""+"y"+s"""$RESET$WHITE   A   B   C   D   E   F   G   H   I   J  
        $WHITE_B$BLACK"""+"x"+s"""$RESET$WHITE    _______________________________________
        """
        this.positions.zipWithIndex.foreach {
            case(x,i) => {
                if(i != 0) res = res.concat("|\n        "+i+"   ")
                else res = res.concat("0   ")
                x.foreach { x => {
                    res = res.concat("|").concat(this.getSelfCaseElement(x))
                }}
            }
        }
        res = res.concat("|\n")
        res
    }

    /**
        Return the case element formatted according to the symbol given. The 
        case element is intended to be seen by the owner of the grid (first letter
        of boat displated for example).        
        @example 
            "W" => "___"
            "X_hit" => """$RED█X$WHITE"""
    */
    def getSelfCaseElement(symbol: String): String = {
        if(symbol == Grid.WATER) "___"
        else {
            // Hit cell
            if(symbol.contains(Grid.HIT_SUFFIX)) {
                val letter = symbol.substring(0, symbol.indexOf(Grid.HIT_SUFFIX))
                if(letter == Grid.WATER) s"""$CYAN███$WHITE"""
                else s"""$RED█$letter█$WHITE"""
            } else {
                "_" + symbol + "_"
            }
        }
    }
}

object Grid {
    def DEFAULT_SIZE: Int = 10
    def HIT_SUFFIX: String = "_hit"
    def VALID_DIRECTIONS: List[String] = List("S", "N", "W", "E")
    def WATER: String = "W"
    def WATER_HIT: String = "W_hit"
}

// ===== Grid exceptions 
case class InvalidCoordinateException(private val message: String = "", 
    private val cause: Throwable = None.orNull) extends Exception(message, cause) 

case class InvalidDirectionException(private val message: String = "", 
    private val cause: Throwable = None.orNull) extends Exception(message, cause)

case class ShipOverlapsException(private val message: String = "", 
    private val cause: Throwable = None.orNull) extends Exception(message, cause) 
    
case class ShipOutOfGridException(private val message: String = "", 
    private val cause: Throwable = None.orNull) extends Exception(message, cause)  