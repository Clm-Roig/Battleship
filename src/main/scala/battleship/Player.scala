package battleship

trait Player {
    val myGrid: Grid
    val name: String
    val score: Int
    val output: Option[Output]
    val input: Option[Input]
    val shotsFired: Array[(Int,Int, String)]

    // Can't find a better solution since you cant copy() a trait or an abstract class...
    // TODO : find something else... Library Shapeless ?
    def copyWithNewGrid(myGrid: Grid): Player
    def copyWithNewScore(score: Int): Player

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