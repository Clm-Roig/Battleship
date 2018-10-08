package battleship
import scala.util.Random
import scala.annotation.tailrec

/**
    AI Level Hard. 
    placeShip => randomness
    shoot =>    if the AI hit a ship before, try to shoot around it to sank it.
                else shoot randomly.
                In any case, never shoot somewhere it shot before.
*/
case class AIHard(name: String = "AI Level Hard", myGrid: Grid = new Grid(), score: Int = 0,
    input: Option[Input] = None, output: Option[Output] = Some(MockConsoleOutput), 
    shotsFired: Set[(Int,Int,String)] = Set()) extends Player {

    override def copyWithNewGrid(myGrid: Grid): Player = this.copy(myGrid = myGrid)
    override def copyWithNewScore(score: Int): Player = this.copy(score = score)
    override def copyWithNewShotsFired(shotsFired: Set[(Int,Int,String)]): Player = this.copy(shotsFired = shotsFired)
    override def emptyShotsFired: Player = this.copy(shotsFired = Set())

    /**
        Ask to shoot. Shot around a cell already hit if possible, else randomness.
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

        // Return coords next to previously hit coords (can be None)
        @tailrec
        def getCoordsNextToShot_rec(tuplesShot: Set[(Int,Int,String)]): Option[(Int,Int)] = {
            if(tuplesShot.size == 0) None
            else {
                val tupleShot = tuplesShot.head
                val possibleCoords = List(
                    (tupleShot._1 + 1, tupleShot._2),
                    (tupleShot._1 - 1, tupleShot._2),
                    (tupleShot._1, tupleShot._2 - 1),
                    (tupleShot._1, tupleShot._2 + 1)
                )

                // Keep on grid coords only
                val possibleCoords_inGrid = possibleCoords.filter(tuple => {
                    tuple._1 >= 0 && tuple._1 <= this.myGrid.size - 1 &&
                    tuple._2 >= 0 && tuple._2 <= this.myGrid.size - 1
                })

                // Keep on grid coords not already hit only
                val coords_opt = possibleCoords_inGrid.filter(possibleTuple => {
                    !this.shotsFired.exists( x => {
                        x._1 == possibleTuple._1 && x._2 == possibleTuple._2 
                    })
                })

                if(coords_opt.length != 0)
                    Some((coords_opt(0)._1, coords_opt(0)._2))
                else getCoordsNextToShot_rec(tuplesShot.tail)
            }
        }
        // Get all tuples shot
        val tuplesShot: Set[(Int,Int,String)] = this.shotsFired.filter( tuple => {
            tuple._3 == Grid.HIT
        })

        // Next to a "hit" coords or random coords (not already fired)
        getCoordsNextToShot_rec(tuplesShot).getOrElse(getCoordsNeverShot_rec(-1,-1,true))
    }

    /**
        Ask to place a ship (random until ok).
    */
    override def askToPlaceAShip(ship: Ship): Player = {
        val newGrid = getNewGridWithShipPlaced(ship).copy(output = MockConsoleOutput)        
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
