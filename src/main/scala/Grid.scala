package battleship

/**
    The square grid used for the Battleship, where the players put their ships. 
    At initialization, the grid is filled with water ("W" char).
    | 
   -|-------------- x ---------------->
    |       0   1   2   3   4   5 ...
    |      _______________________
    |   0 |_W_|_W_|_W_|_W_|_W_|_W_|         N
    y   1 |_W_|_W_|_W_|_W_|_W_|_W_|         Λ
    |   2 |_W_|_W_|_W_|_W_|_W_|_W_|    W <--|--> E
    |   3 |_W_|_W_|_W_|_W_|_W_|_W_|         V
    |   ...                                 S
    V

    @param ships ships on the grid
    @param size size of the grid (default: 10)
    @param positions 2 dimensions array representing 
        the cells of the grid, each cell can be water or ship (hit or not for both).
 */
case class Grid (ships: Array[Ship], size: Int, positions: Array[Array[String]]) {

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

    @throws InvalidCoordinateException if x or y are not between 0 and 9
    @throws InvalidDirectionException if direction is not part of VALID_DIRECTIONS
    @return Option[Grid], Some if the Ship was correctly added, else None.
    */
    def addShip(x: Int, y: Int, s: Ship, direction: String): Grid = {
        // Tests data provided
        if(x > 9|| x < 0) throw new InvalidCoordinateException("x must be between 0 and 9.")
        if(y > 9|| y < 0) throw new InvalidCoordinateException("y must be between 0 and 9.")
        if(!Grid.VALID_DIRECTIONS.contains(direction)) 
            throw new InvalidDirectionException("direction must be a value in [\"" + (Grid.VALID_DIRECTIONS mkString "\", \"") + "\"].")

        // Attempt to place the Ship
        val cellsToCheck = this.getCellsToCheck(x,y,direction,s.size)
        
        // Test if it overlaps a Ship
        if (cellsToCheck.forall(tuple => this.isShipHere(tuple._1, tuple._2)))
            throw new ShipOverlapsException("The Ship you are trying to add is overlapping another one.")

        // Test if it is out of grid
        if (cellsToCheck.forall(tuple => {
            tuple._1 < 0||tuple._2 < 0||tuple._1 >= this.size||tuple._2 >= this.size
        })) throw new ShipOutOfGridException("The Ship you are trying to add is out of the grid.")

        new Grid()
    }

    /**
        Return the cells passed through from the coordinate(x,y) according to the 
        direction and the nbOfCells given. The initial cell is not included in the result.
        @param x x coordinate of the initial cell
        @param y y coordinate of the initial cell
        @param direction direction of the next cell, must be in VALID_DIRECTIONS
        @param nbOfCells number of cells to pass through
        @param previousCells celles passed through previously

        @return Array[(Int,Int)] the cells coordinates (x,y) passed through.
    */
    def getCellsToCheck(x: Int, y: Int, direction: String, nbOfCells: Int, 
    previousCells: Array[(Int, Int)] = Array()): Array[(Int, Int)] = {
        if(nbOfCells <= 0) previousCells
        else {
            val nextCell = this.nextCell(x,y,direction)
            val cells = previousCells :+ nextCell
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
        this.positions(x)(y) != Grid.WATER && this.positions(x)(y) != Grid.WATER_HIT
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
        val xCoord = direction match {
            case "W" => x - 1
            case "E" => x + 1
            case _ => x
        }
        val yCoord = direction match {
            case "N" => y - 1
            case "S" => y + 1
            case _ => y
        }
        (xCoord, yCoord)
    }
}

object Grid {
    def DEFAULT_SIZE = 10
    def VALID_DIRECTIONS = List("S", "N", "W", "E")
    def WATER = "W"
    def WATER_HIT = "W_hit"
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