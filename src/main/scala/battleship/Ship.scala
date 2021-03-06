package battleship
/**
    A ship used on the grid to play Battleship
    @param name name of the ship
    @param symbol symbol which represents the ship (usually, the first letter of the name)
    @param size size of the ship (in number of cells on the grid)
    @param lifePoints ship life points remaining. At the beginning: lifePoints = size
 */
case class Ship (name: String, symbol: String, size: Int, lifePoints: Int) {
    /**
    If no lifePoints provided, it's equal to the size.
    */
    def this(name: String, symbol: String, size: Int) = {
        this(name, symbol, size, size)
    }

    /**
    Returns true if the Ship is sunk i.e, his lifePoints equals to 0. 
    */
    def isSunk(): Boolean = this.lifePoints == 0

    /**
    Returns a new Ship with lifePoints decremented by one. If lifePoints == 0, it remains 0.  
    */
    def hit(): Ship = {
        val newLifePoints = if(this.lifePoints > 0) this.lifePoints - 1 else 0 
        this.copy(lifePoints = newLifePoints)
    }

    // Ship toString representation: Name[size]
    override def toString: String = {
        this.name + "[" + this.size + "]"
    }
}