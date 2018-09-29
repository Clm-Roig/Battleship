package battleship

/**
    The square grid used for the Battleship, where the players put their ships. 
    @param ships ships on the grid
    @param size size of the grid (default: 10)
    @param positions 2 dimensions array representing 
        the cells of the grid, each cell can be water or ship (hit or not for both).
 */
case class Grid (ships: Array[Ship] = Array(), size: Int = 10, 
    positions: Array[Array[String]] = Array.ofDim[String](2,2)) {
   
   /**
    Add a ship to the grid. The ship will be placed at (x,y) and facing the direction given.
    @param x x coordinate, equivalent to A,B,C,...,J converted in Int
    @param y y coordinate
    @param s Ship to place on the Grid 
    @param direction direction of the ship

    @throws CoordinateException if x or y are not between 0 and 9
    @throws InvalidDirectionException if direction is not part of VALID_DIRECTIONS
    @return Option[Grid], Some if the Ship was correctly added, else None.
    */
    def addShip(x: Int, y: Int, s: Ship, direction: String): Option[Grid] = {
        // Tests data provided
        if(x > 9|| x < 0) throw new CoordinateException("x must be between 0 and 9.")
        if(y > 9|| y < 0) throw new CoordinateException("y must be between 0 and 9.")
        if(!Grid.VALID_DIRECTIONS.contains(direction)) 
            throw new InvalidDirectionException("direction must be a value in [\"" + (Grid.VALID_DIRECTIONS mkString "\", \"") + "\"].")

        Some(Grid())
    }

    /**
   
    */
    def isShipHere(x: Int, y: Int): Boolean = {
        return true
    }

}

object Grid {
    def VALID_DIRECTIONS = List("S", "N", "W", "E")
}

// Grid exceptions 
case class CoordinateException(private val message: String = "", 
    private val cause: Throwable = None.orNull) extends Exception(message, cause) 

case class InvalidDirectionException(private val message: String = "", 
    private val cause: Throwable = None.orNull) extends Exception(message, cause) 