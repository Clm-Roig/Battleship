package battleship
import scala.util.Random
import scala.annotation.tailrec

case class AIMedium(name: String = "AI Level Medium", myGrid: Grid = new Grid(), score: Int = 0,
    input: Option[Input] = None, output: Option[Output] = Some(MockConsoleOutput), 
    shotsFired: Set[(Int,Int,String)] = Set()) extends Player {

    override def copyWithNewGrid(myGrid: Grid): Player = this.copy(myGrid = myGrid)
    override def copyWithNewScore(score: Int): Player = this.copy(score = score)
    override def copyWithNewShotsFired(shotsFired: Set[(Int,Int,String)]): Player = this.copy(shotsFired = shotsFired)
    override def emptyShotsFired: Player = this.copy(shotsFired = Set())

    /**
        Ask to shoot (random shoot).
    */
    override def askForShootCoordinates(): (Int,Int) = { 

        // Return only coordinates where the AI didn't shot before.
        @tailrec    
        def getCoordsNeverShot_rec(x: Int, y: Int, alreadyShot: Boolean): (Int,Int) = {
            if(!alreadyShot) (x,y)
            else {
                val newX = (new Random()).nextInt(this.myGrid.size)
                val newY = (new Random()).nextInt(this.myGrid.size)
                val newAlreadyShot = this.shotsFired.exists(shot => {
                    (shot._1 == newX) && (shot._2 == newY)
                })
                getCoordsNeverShot_rec(newX, newY, newAlreadyShot)
            }
        }
        getCoordsNeverShot_rec(-1,-1,true)        
    }

    /**
        Ask to place a ship (random until ok)
    */
    override def askToPlaceAShip(ship: Ship): Player = {
        val x = (new Random).nextInt(this.myGrid.size)
        val y = ((new Random).nextInt(this.myGrid.size) + 'A').toChar
        val dirInt = (new Random).nextInt(4)
        val dir = Grid.VALID_DIRECTIONS(dirInt)
        try {
            val newGrid = this.myGrid.addShip(x,y,ship,dir)
            this.copy(myGrid = newGrid)
        } catch {
            case e: Exception => {
                askToPlaceAShip(ship)
            }
        }
    }
}