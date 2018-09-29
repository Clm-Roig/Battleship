package battleship

case class Human(name: String) extends Player {
    def askForShootCoordinates(): (Int,Int) = {
        // TODO
        (1,1)
    }

    def askToPlaceAShip(ship: Ship): (Int,Int,String) = {
        // TODO
        (1,1,"S")
    }

}