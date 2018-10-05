package battleship
import scala.util.Random

/**
    AI Level Low. 
    placeShip => randomness
    shoot => randomness (can be somewhere it shots before)
*/
case class AILow(name: String = "AI Level Beginner", myGrid: Grid = new Grid(), score: Int = 0,
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
        val x = (new Random).nextInt(this.myGrid.size)
        val y = (new Random).nextInt(this.myGrid.size)
        (x,y)
    }

    /**
        Ask to place a ship (random until ok).
    */
    override def askToPlaceAShip(ship: Ship): Player = {
        val newGrid = getNewGridWithShipPlaced(ship)      
        this.copy(myGrid = newGrid)    
    }

    /**
        Place a Ship randomly on the grid and return the new grid updated.
    */
    def getNewGridWithShipPlaced(ship: Ship): Grid = {
        val x = (new Random).nextInt(this.myGrid.size)
        val y = ((new Random).nextInt(this.myGrid.size) + 'A').toChar
        val dirInt = (new Random).nextInt(4)
        val dir = Grid.VALID_DIRECTIONS(dirInt)
        this.myGrid.addShip(x,y,ship,dir,this.output.get).getOrElse(
            getNewGridWithShipPlaced(ship)
        )
    }
}