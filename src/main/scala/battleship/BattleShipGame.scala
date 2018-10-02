package battleship
import scala.util.Random

object BattleSchipGame extends App {
    // ===== CONST
    val GAME_TYPES = Array(
        "Human vs Human",
        "Human vs IA low",
        "Human vs IA medium",
        "Human vs IA hard",
        "IA low vs IA medium",
        "IA medium vs IA hard",
        "IA low vs IA hard"
    )

    val SHIPS = Array(
        new Ship("Carrier","C",5),
        new Ship("Battleship","B",4),
        new Ship("Cruiser","c",3),
        new Ship("Submarine","S",3),
        new Ship("Destroyer","D",2),
    )

    val output = ConsoleOutput

    start()

    /**
        Start the Battleship program.
    */
    def start(): Unit = {

        // Init
        output.clear()
        output.display("====================")
        output.display("==== BATTLESHIP ====")
        output.display("====================")
        val gameType = askForGameType()

        // TODO : case depending on gameType
        output.clear()
        output.display("Player 1 name:")
        val p1Name = scala.io.StdIn.readLine()
        val p1 = new Human(p1Name)
        output.display("Player 2 name:")
        val p2Name = scala.io.StdIn.readLine()
        val p2 = new Human(p2Name)

        val beginner = (new Random).nextInt(2)

        // Place ships
        val player1: Player = this.getNewPlayerWithShipsPlaced(SHIPS, p1)
        output.clear()
        output.display(player1.myGrid.toStringToSelf())
        output.display("Press any key to let " + p2Name + " place his ships.")
        scala.io.StdIn.readLine()
        output.clear()

        val player2: Player = this.getNewPlayerWithShipsPlaced(SHIPS, p2)   
        output.clear()
        output.display(player2.myGrid.toStringToSelf())
        output.display("Press any key to start the battle.")
        scala.io.StdIn.readLine()
        output.clear() 
        
        // Launch the battle
        output.display("\nThe battle between " + p1.name + " & " + p2.name + " begins!")
        if (beginner == 0) output.display(p1.name + " starts.") else output.display(p2.name + " starts.")
        val state = GameState(new Human(name = p1.name), new Human(name = p2.name), beginner, beginner)
        gameLoop(state)
    }

    // Game loop (turn after turn)
    def gameLoop(state: GameState) {
        
        // Check if game is over

        // Shoot
        println("Turn!")

        // Update data by creating new objects 
        val newP1 = new Human("toto")
        val newP2 = new Human("tata")
        val newCurrentPlayer = if(state.currentPlayer == 0) 1 else 0
        val newState = state.copy(player1 = newP1, player2 = newP2, currentPlayer = newCurrentPlayer)
        gameLoop(newState)        
    }

    /**
        Return a new Player with the list of ships placed on his grid.
    */
    def getNewPlayerWithShipsPlaced(ships: Array[Ship], p: Player): Player = {  
        if(ships.length == 0) p
        else {
            val ship = ships.last
            output.clear()
            output.display(p.name + " place the ship: " + ship.toString)
            val newPlayer = p.askToPlaceAShip(ship)
            getNewPlayerWithShipsPlaced(ships.take(ships.length - 1), newPlayer)
        }        
    }

    /**
        Ask the user the game type he wants: 
    */
    def askForGameType(): Int = {
        output.display("Choose your game type (integer only):")
        GAME_TYPES.zipWithIndex.foreach{ 
            case(x, i) => output.display(i + ". " + x)
        }
        val valueTyped = scala.io.StdIn.readLine()
        try {
            val intTyped = Integer.parseInt(valueTyped)
            if(intTyped < 0 || intTyped >= this.GAME_TYPES.length) askForGameType()
            else intTyped
        } catch {
            case e: NumberFormatException => askForGameType()
        }
    }
}