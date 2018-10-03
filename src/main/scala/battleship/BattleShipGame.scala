package battleship
import scala.util.Random
import scala.annotation.tailrec

object BattleSchipGame extends App {
    // ===== CONST
    val GAME_TYPES = Array(
        "Human vs Human",
        "Human vs AI (low)",
        "Human vs AI (medium)",
        //"Human vs AI (hard)",
    )

    val SHIPS = Array(
        new Ship("Carrier","C",5),
        new Ship("Battleship","B",4),
        new Ship("Cruiser","c",3),
        new Ship("Submarine","S",3),
        new Ship("Destroyer","D",2)
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

        gameType match {
            // Human VS Human
            case 0 => {
                // Enter players name
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
                val state = if (beginner == 0) {
                    output.display(player1.name + " starts.") 
                    new GameState(player1, player2, beginner)
                } else {
                    output.display(player2.name + " starts.")
                    new GameState(player1, player2, beginner)
                }
                val lastState = gameLoop(state)
            }
            // Human VS AI (low)
            case 1|2 => {
                // Enter player name
                output.clear()
                output.display("Player 1 name:")
                val p1Name = scala.io.StdIn.readLine()
                val p1 = new Human(p1Name)

                val p2: Player = gameType match {
                    case 1 => new AILow()
                    case 2 => new AIMedium()
                }

                val beginner = (new Random).nextInt(2)

                // Place ships
                val player1: Player = this.getNewPlayerWithShipsPlaced(SHIPS, p1)
                output.clear()
                output.display(player1.myGrid.toStringToSelf())
                output.display("Press any key to let " + p2.name + " place his ships.")
                scala.io.StdIn.readLine()
                output.clear()

                val player2: Player = this.getNewPlayerWithShipsPlaced(SHIPS, p2)   
                
                // Launch the battle
                val state = if (beginner == 0) {
                    output.display(player1.name + " starts.") 
                    new GameState(player1, player2, beginner)
                } else {
                    output.display(player2.name + " starts.")
                    new GameState(player1, player2, beginner)
                }
                val lastState = gameLoop(state)
                output.clear()
                printFinalResult(lastState)
            }
            case _ => {
                output.displayError("Unkown game type.")
            }
        }        
    }

    // Game loop (play another game ?)
    @tailrec
    def gameLoop(state: GameState): GameState = {
        if(state.nbOfGames != 0) {
            if(this.askForAnotherGame()) {
                output.clear()
                
                val p1 = state.player1.copyWithNewGrid(myGrid = new Grid()) 
                val p2 = state.player2.copyWithNewGrid(myGrid = new Grid()) 

                // TODO : refactor all this part (see end of start() method)
                // Place ships
                val player1: Player = this.getNewPlayerWithShipsPlaced(SHIPS, p1).emptyShotsFired() 
                output.clear()
                output.display(player1.myGrid.toStringToSelf())
                output.display("Press any key to let " + p2.name + " place his ships.")
                scala.io.StdIn.readLine()
                output.clear()

                val player2: Player = this.getNewPlayerWithShipsPlaced(SHIPS, p2).emptyShotsFired() 
                output.clear()
                output.display(player2.myGrid.toStringToSelf())
                output.display("Press any key to start the battle.")
                scala.io.StdIn.readLine()
                output.clear()

                // Launch the battle
                val newState = if (state.playerWhoBegins == 0) {
                    output.display(player2.name + " starts.") 
                    state.copy(player1 = player1, player2 = player2, currentPlayer = player2, playerWhoBegins = 1)
                } else {
                    output.display(player1.name + " starts.")
                    state.copy(player1 = player1, player2 = player2, currentPlayer = player1, playerWhoBegins = 0)
                }

                gameLoop(battleLoop(newState))
            }
            else state
        } else {
            // launch the first game
            gameLoop(battleLoop(state))
        }
    }

    // Battle loop (turn after turn)
    @tailrec
    def battleLoop(state: GameState): GameState = {
        val nextPlayer = if(state.currentPlayer == state.player1) state.player2 else state.player1
        val currentPlayer = state.currentPlayer

        // Shoot
        output.display("It's " + currentPlayer.name + "'s turn!")
        currentPlayer.output.get.display(currentPlayer.myGrid.toStringToSelf())

        val coords = currentPlayer.askForShootCoordinates(nextPlayer.myGrid)
        output.display(currentPlayer.name + " shoots at (" + coords._1 + "," + (coords._2 + 'A').toChar +")...")
        val shotResult = nextPlayer.myGrid.shootHere(coords._1, coords._2)
        val ship: Option[Ship] = shotResult._1
        val shipName = ship.getOrElse("")
        val shotState: String = shotResult._2
        val newGrid: Grid = shotResult._3

        val lastShot: (Int,Int,String) = (coords._1, coords._2, shotState)
        val newShotsFired = currentPlayer.shotsFired + lastShot

        if(shotState == Grid.MISS) output.display("Missed!")
        if(shotState == Grid.HIT) output.display(shipName + " hit!")
        if(shotState == Grid.SUNK) output.display(shipName + " sunk!!")
        
        // Update data by creating new objects 
        val nextPlayerWithGridUpdated = nextPlayer.copyWithNewGrid(myGrid = newGrid)
        currentPlayer.output.get.display(nextPlayerWithGridUpdated.myGrid.toStringToOpponent())

        val newCurrentPlayer = currentPlayer.copyWithNewShotsFired(shotsFired = newShotsFired)

        // Check if game is over
        if(nextPlayerWithGridUpdated.myGrid.areAllShipsSunk()) {
            output.display("All " + nextPlayer.name + "'s ships are sunk, " + currentPlayer.name + " wins! Congratulations!")

            // Increase score of currentPlayer
            val newCurrentPlayerUpdated = newCurrentPlayer.copyWithNewScore(score = currentPlayer.score + 1).copyWithNewShotsFired(shotsFired = newShotsFired)
            
            // Player1 saved as player1 (same for p2)
            val lastState = if(state.currentPlayer == state.player1) 
                    state.copy(player1 = newCurrentPlayerUpdated, player2 = nextPlayerWithGridUpdated, nbOfGames = state.nbOfGames + 1)
                else state.copy(player1 = nextPlayerWithGridUpdated, player2 = newCurrentPlayerUpdated, nbOfGames = state.nbOfGames + 1)

            output.display(lastState.nbOfGames + " game(s) played.")
            output.display("===== SCORE =====\n" 
                + newCurrentPlayerUpdated.name + ": " + newCurrentPlayerUpdated.score 
                + "\n" + nextPlayerWithGridUpdated.name + ": " + nextPlayerWithGridUpdated.score 
            )

            return lastState
        }

        // Prepare for next turn
        if(newCurrentPlayer.isInstanceOf[Human]) {
            currentPlayer.output.get.display("Press any key to let " + nextPlayer.name + " plays.")
            scala.io.StdIn.readLine()
            output.clear()    
        }
        if(newCurrentPlayer == state.player1) 
            battleLoop(state.copy(player1 = newCurrentPlayer, player2 = nextPlayerWithGridUpdated, currentPlayer = nextPlayerWithGridUpdated))
        else 
            battleLoop(state.copy(player1 = nextPlayerWithGridUpdated, player2 = newCurrentPlayer, currentPlayer = nextPlayerWithGridUpdated))
    }

    /**
        Return a new Player with the list of ships placed on his grid.
    */
    @tailrec
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

    def printFinalResult(state: GameState): Unit = {
        output.display(state.nbOfGames + " game(s) played.")
        output.display("===== SCORE =====\n" 
            + state.player1.name + ": " + state.player1.score 
            + "\n" + state.player2.name + ": " + state.player2.score 
        )   
    }
    
    def askForAnotherGame(): Boolean = {
        output.display("Do you want to play another game ? (y/n)")
        val valueTyped = scala.io.StdIn.readLine()
        try {
            val char = valueTyped.toUpperCase()(0)
            char match {
                case 'Y' => true
                case 'N' => false
                case _ => {
                    output.displayError("Enter y or n.")
                    askForAnotherGame()
                }
            }
        } catch {
            case e : StringIndexOutOfBoundsException => {
                output.displayError("Enter y or n please.")
                askForAnotherGame()
            }
            case e : Throwable => {
                output.displayError("An unexpected exception occured, please try again.")
                askForAnotherGame()
            }
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