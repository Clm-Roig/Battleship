package battleship

trait Player {
    val myGrid: Grid
    val name: String
    val score: Int

    /**
        Ask the player to enter coordinate for a shoot.
        @param opponentGrid Grid of the opponent

        @return (Int,Int) x coordinate, y coordinate
    */
    def askForShootCoordinates(opponentGrid: Grid): (Int,Int)

    /**
        Ask the player to place a Ship. 
        @param ship Ship to place

        @return Player, a new Player with the Ship placed
    */
    def askToPlaceAShip(ship: Ship): Player
}