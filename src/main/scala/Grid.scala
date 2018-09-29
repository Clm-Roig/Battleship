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
    @param x x coordinate, equivalent A,B,C,...,J converted in Int,
        Must be between 0 and 9
    @param y y coordinate, must be between 0 and 9
    @param s Ship to place on the Grid 
    @param direction direction of the ship, must € ["S", "W", "E", "N"]

    @return Option[Grid], Some if the Ship was correctly added, else None.
    */
   def addShip(x: Int, y: Int, s: Ship, direction: String): Option[Grid] = {
        val newPos = this.positions
        newPos(0)(0) = "test"
        val newGrid: Grid = this.copy(positions = newPos)
        return Some(newGrid)
   }

   /**
   
    */
   def isShipHere(x: Int, y: Int): Boolean = {
       return true
   }

}