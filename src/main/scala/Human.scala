package battleship

case class Human(name: String, myGrid: Grid = new Grid(), score: Int = 0, output: Output = ConsoleOutput) extends Player {

    def askForShootCoordinates(): (Int,Int) = {
        // TODO
        val valueTyped = scala.io.StdIn.readLine()
        
        (1,1)
    }

    def askToPlaceAShip(ship: Ship): Player = {
        output.display(this.myGrid.toStringToSelf())
        // x coordinate
        output.display("Enter the x coordinate of the ship (between 0 and " + (this.myGrid.size - 1) + ").")
        val x = this.askToEnterXCoordinate()

        // y coordinate
        output.display("Enter the y coordinate of the ship.")
        val y = this.askToEnterYCoordinate()

        //TODO : convert y to int

        // Direction
        output.display("Enter the direction (N, S, E or W) of the ship.")
        val dir = this.askToEnterDirection()
        try {
            val newGrid = this.myGrid.addShip(x,y,ship,dir)
            new Human(this.name, newGrid)
        } catch {
            case e: Exception => {
                output.display(e.toString)
                askToPlaceAShip(ship)
            }
        }
    }

    /**
        Return the x coordinate asked to the Human via Console prompt.
    */
    def askToEnterXCoordinate(): Int = {
        val xTyped = scala.io.StdIn.readLine()
        try {
            val x = xTyped.toInt
            if(x < 0 ||x >= this.myGrid.size) {
                output.display("x must be between 0 and " + (this.myGrid.size - 1) + ".")
                askToEnterXCoordinate()
            }
            x
        } catch {
            case e: NumberFormatException => output.display("x must be an integer between 0 and " + (this.myGrid.size - 1) + ".")            
            askToEnterXCoordinate()
        }
    }

    /**
        Return the y coordinate asked to the Human via Console prompt.
    */
    def askToEnterYCoordinate(): Char = {
        val yTyped = scala.io.StdIn.readLine()
        try {
            val y = yTyped(0)
            if(!y.isLetter) {
                output.display("y must be a letter.")
                askToEnterYCoordinate()
            }
            y
        } catch {
            case _ : Throwable => {
                output.display("Unexpected exception.")
                askToEnterYCoordinate()
            }
        }
    }

    /**
        Return the direction asked to the Human via Console prompt.
    */
    def askToEnterDirection(): String = {
        val dirTyped = scala.io.StdIn.readLine()
        try {
            val dir = dirTyped.toUpperCase()(0).toString
            if(!Grid.VALID_DIRECTIONS.contains(dir)) {
                output.display("Direction must be "+ (Grid.VALID_DIRECTIONS mkString ", ") + ".")
                askToEnterDirection()
            }
            dir
        } catch {
            case _ : Throwable => {
                output.display("Unexpected exception.")
                askToEnterDirection()
            }
        }
    }

}