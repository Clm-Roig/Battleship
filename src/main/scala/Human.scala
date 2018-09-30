package battleship

case class Human(name: String) extends Player {
    def askForShootCoordinates(): (Int,Int) = {
        // TODO
        val valueTyped = scala.io.StdIn.readLine()
        
        (1,1)
    }

    def askToPlaceAShip(ship: Ship): (Int,Int,String) = {
        // TODO
        val valueTyped = scala.io.StdIn.readLine()
        try {
            val intTyped = Integer.parseInt(valueTyped)
            if(intTyped < 0 || intTyped >= this.myGrid.size) askToPlaceAShip(ship)
            else intTyped
        } catch {
            case e: NumberFormatException => askToPlaceAShip(ship)
        }
        (1,1,"S")
    }

}