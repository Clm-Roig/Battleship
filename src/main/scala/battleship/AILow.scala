package battleship
import scala.util.Random

case class AILow(name: String = "AI low", myGrid: Grid = new Grid(), score: Int = 0,
    input: Option[Input] = None, output: Option[Output] = Some(MockConsoleOutput), 
    shotsFired: Set[(Int,Int,String)] = Set()) extends Player {

    override def copyWithNewGrid(myGrid: Grid): Player = this.copy(myGrid = myGrid)
    override def copyWithNewScore(score: Int): Player = this.copy(score = score)
    override def copyWithNewShotsFired(shotsFired: Set[(Int,Int,String)]): Player = this.copy(shotsFired = shotsFired)
    override def emptyShotsFired: Player = this.copy(shotsFired = Set())

    /**
        Ask to shoot (random shoot).
    */
    override def askForShootCoordinates(opponentGrid: Grid): (Int,Int) = {     
        val x = (new Random).nextInt(this.myGrid.size)
        val y = (new Random).nextInt(this.myGrid.size)
        return (x,y)
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