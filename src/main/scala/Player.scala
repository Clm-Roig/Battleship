package battleship

trait Player {
    val myGrid = new Grid()
    val name: String
    val score  = 0

    /**
        Ask the player to enter coordinate for a shoot.
        @return (Int,Int) x coordinate, y coordinate
    */
    def askForShootCoordinates(): (Int,Int)

    /**
        Ask the player to place a Ship. 
        @return (Int,Int,String) x coordinate, y coordinate, direction
    */
    def askToPlaceAShip(ship: Ship): (Int,Int,String)
}