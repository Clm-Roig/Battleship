package battleship

/**
    A Player is something which can play at BattleShip. It should be able to place a ship
    and to return coordinates to shot.
*/
trait Player {
    val myGrid: Grid
    val name: String
    val score: Int
    val output: Option[Output]
    val input: Option[Input]
    val shotsFired: Set[(Int,Int, String)]

    // Can't find a better solution since you cant copy() a trait or an abstract class...
    // TODO : find something else... Library Shapeless ? https://github.com/milessabin/shapeless
    def copyWithNewGrid(myGrid: Grid): Player
    def copyWithNewScore(score: Int): Player
    def copyWithNewShotsFired(shotsFired: Set[(Int,Int,String)]): Player

    // Reset the shotsFired property    
    def emptyShotsFired(): Player

    /**
        Ask the player to enter coordinate for a shoot.
        
        @return (Int,Int) x coordinate, y coordinate
    */
    def askForShootCoordinates(): (Int,Int)

    /**
        Ask the player to place a Ship. 
        @param ship Ship to place

        @return Player, a new Player with the Ship placed
    */
    def askToPlaceAShip(ship: Ship): Player
}