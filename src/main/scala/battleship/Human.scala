package battleship

case class Human(name: String, myGrid: Grid = new Grid(), score: Int = 0, 
output: Output = ConsoleOutput, input: Input = ConsoleInput) extends Player {

    /**
        Ask the human player to shoot by entering the coordinates desired.
    */
    def askForShootCoordinates(opponentGrid: Grid): (Int,Int) = {        
        output.display(opponentGrid.toStringToOpponent())
        // x coordinate
        output.display("Enter the x coordinate (between 0 and " + (this.myGrid.size - 1) + ").")
        val x = this.askToEnterXCoordinate()

        // y coordinate
        output.display("Enter the y coordinate (between A and " + (this.myGrid.size + 'A' - 1).toChar + ").")
        val yChar = this.askToEnterYCoordinate()
        val y = yChar.toUpper.toInt - 'A'.toInt

        return (x,y)
    }

    /**
        Ask the human player to place a ship by typing the coordinates desired.
    */
    def askToPlaceAShip(ship: Ship): Player = {
        output.display(this.myGrid.toStringToSelf())
        // x coordinate
        output.display("Enter the x coordinate of the ship (between 0 and " + (this.myGrid.size - 1) + ").")
        val x = this.askToEnterXCoordinate()

        // y coordinate
        output.display("Enter the y coordinate of the ship (between A and " + (this.myGrid.size + 'A' - 1).toChar + ").")
        val y = this.askToEnterYCoordinate()

        // Direction
        output.display("Enter the direction (N, S, E or W) of the ship.")
        val dir = this.askToEnterDirection()
        try {
            val newGrid = this.myGrid.addShip(x,y,ship,dir)
            this.copy(myGrid = newGrid)
        } catch {
            case e: Exception => {
                output.displayError(e.getMessage)
                askToPlaceAShip(ship)
            }
        }
    }

    /**
        Return the x coordinate asked to the Human via Console prompt.
    */
    def askToEnterXCoordinate(): Int = {
        val xTyped = input.askString()
        try {
            val x = xTyped.toInt
            if(x < 0 ||x >= this.myGrid.size) {
                output.displayError("x must be between 0 and " + (this.myGrid.size - 1) + ".")
                askToEnterXCoordinate()
            }
            else {
                x
            }
        } catch {
            case e: NumberFormatException => output.displayError("x must be an integer between 0 and " + (this.myGrid.size - 1) + ".")            
            askToEnterXCoordinate()
        }
    }

    /**
        Return the y coordinate asked to the Human via Console prompt.
    */
    def askToEnterYCoordinate(): Char = {
        val yTyped = input.askString()
        try {
            val y = yTyped.toUpperCase()(0)
            if(!y.isLetter) {
                output.displayError("y must be a letter.")
                askToEnterYCoordinate()
            }
            else if((y - (this.myGrid.size + 'A')).toInt >= 0) {
                output.displayError("y must be between A and "+ (this.myGrid.size + 'A' - 1).toChar +".")
                askToEnterYCoordinate()
            }
            else {
                y
            }
        } catch {
            case e : StringIndexOutOfBoundsException => {
                output.displayError("Enter a letter please.")
                askToEnterYCoordinate()
            }
            case e : Throwable => {
                output.displayError("An unexpected exception occured, please try again.")
                askToEnterYCoordinate()
            }
        }
    }

    /**
        Return the direction asked to the Human via Console prompt.
    */
    def askToEnterDirection(): String = {
        val dirTyped = input.askString()
        try {
            val dir = dirTyped.toUpperCase()(0).toString
            if(!Grid.VALID_DIRECTIONS.contains(dir)) {
                output.displayError("Direction must be "+ (Grid.VALID_DIRECTIONS mkString ", ") + ".")
                askToEnterDirection()
            }
            else {
                dir
            }
        } catch {
            case _ : Throwable => {
                output.display("Unexpected exception.")
                askToEnterDirection()
            }
        }
    }

}