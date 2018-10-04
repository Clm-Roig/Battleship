package battleship
import scala.util.Random
import scala.annotation.tailrec
import Console.{CYAN, GREEN, RED, RESET, WHITE}
import java.io.{BufferedWriter, File, FileWriter}

/**
    Main class used to test the AIs.
*/
object TestAI extends App {
    // ===== CONST
    val GAME_TYPES = Array(
        "AI(low) vs AI (medium)",
        "AI(low) vs AI (hard)",
        "AI(medium) vs AI (hard)",
        "All 3 above"
    )

    val SHIPS = Array(
        new Ship("Carrier","C",5),
        new Ship("Battleship","B",4),
        new Ship("Cruiser","c",3),
        new Ship("Submarine","S",3),
        new Ship("Destroyer","D",2)
    )

    val NB_OF_GAMES_TO_PLAY = 100
    val output = ConsoleOutput

    start()

    /**
        Start the Battleship program.
    */
    def start(): Unit = {

        // Init
        output.clear()
        output.display(s"""
     $WHITE                                   )___(
                                  ______/__/_
                         ___     /===========|      
        ____     =======[|||]___/____________|_    _/_
        \\   \\_____[||]__/______________________\\__[||]___
         \\                                               |
          \\       BATTLESHIP - Clément Roig - IG5        /
        $CYAN~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~$RESET
                  $RED====== AIs Test Program =======$RESET
        """ + "\n")
        val gameType = askForGameType()

        gameType match {
            // AI(low) vs AI (medium)
            case 0 => {
                output.clear()
                val beginner = (new Random).nextInt(2)

                // Place ships
                val player1: Player = this.getNewPlayerWithShipsPlaced(SHIPS, new AILow())   
                val player2: Player = this.getNewPlayerWithShipsPlaced(SHIPS, new AIMedium())   
                
                // Create State and launch the battle
                val lastState = gameLoop(new GameState(player1, player2, beginner))
                outputFinalResult(lastState)
            }
            // AI(low) vs AI (hard)
            case 1 => {
                output.clear()
                val beginner = (new Random).nextInt(2)
                // Place ships
                val player1: Player = this.getNewPlayerWithShipsPlaced(SHIPS, new AILow())   
                val player2: Player = this.getNewPlayerWithShipsPlaced(SHIPS, new AIHard())   
                
                // Create state and launch the battle
                val lastState = gameLoop(new GameState(player1, player2, beginner))
                outputFinalResult(lastState)            
            }
            // AI(medium) vs AI (hard)
            case 2 => {
                output.clear()
                val beginner = (new Random).nextInt(2)
                // Place ships
                val player1: Player = this.getNewPlayerWithShipsPlaced(SHIPS, new AIMedium())   
                val player2: Player = this.getNewPlayerWithShipsPlaced(SHIPS, new AIHard())   
                
                // Create State and launch the battle
                val lastState = gameLoop(new GameState(player1, player2, beginner))
                outputFinalResult(lastState) 
            }
            // 3 above
            case 3 => {
                output.clear()
                val beginner1 = (new Random).nextInt(2)
                val beginner2 = (new Random).nextInt(2)
                val beginner3 = (new Random).nextInt(2)

                // Place ships for all AI
                val low1: Player = this.getNewPlayerWithShipsPlaced(SHIPS, new AILow())   
                val low2: Player = this.getNewPlayerWithShipsPlaced(SHIPS, new AILow())   
                val medium1: Player = this.getNewPlayerWithShipsPlaced(SHIPS, new AIMedium())  
                val medium2: Player = this.getNewPlayerWithShipsPlaced(SHIPS, new AIMedium())   
                val hard1: Player = this.getNewPlayerWithShipsPlaced(SHIPS, new AIHard())  
                val hard2: Player = this.getNewPlayerWithShipsPlaced(SHIPS, new AIHard())   
                
                // Create States and launch the battles
                val lastState1 = gameLoop(new GameState(low1, medium1, beginner1))
                val lastState2 = gameLoop(new GameState(low2, hard1, beginner2))
                val lastState3 = gameLoop(new GameState(hard2, medium2, beginner3))

                output.clear()
                outputFinalResult(lastState1) 
                output.display("")
                outputFinalResult(lastState2) 
                output.display("")
                outputFinalResult(lastState3)
                output.display("")

                createCSVRecap(Array(lastState1, lastState2, lastState3))
            }
            case _ => {
                output.displayError("Unkown game type.")
            }
        }     
    }

    // Game loop (play another game ?)
    @tailrec
    def gameLoop(state: GameState): GameState = {
        outputNbOfGamesProgress(state)
        if(state.nbOfGames != 0) {
            if(state.nbOfGames < NB_OF_GAMES_TO_PLAY) {
                val p1 = state.player1.copyWithNewGrid(myGrid = new Grid()) 
                val p2 = state.player2.copyWithNewGrid(myGrid = new Grid()) 

                // Place ships
                val player1: Player = this.getNewPlayerWithShipsPlaced(SHIPS, p1).emptyShotsFired() 
                val player2: Player = this.getNewPlayerWithShipsPlaced(SHIPS, p2).emptyShotsFired()    

                // Launch the battle
                val newState = if (state.playerWhoBegins == 0) {
                    state.copy(player1 = player1, player2 = player2, currentPlayer = player2, playerWhoBegins = 1)
                } else {
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
        val coords = currentPlayer.askForShootCoordinates()
        val shotResult = nextPlayer.myGrid.shootHere(coords._1, coords._2)
        val ship: Option[Ship] = shotResult._1
        val shipName = ship.getOrElse("")
        val shotState: String = shotResult._2
        val newGrid: Grid = shotResult._3

        val lastShot: (Int,Int,String) = (coords._1, coords._2, shotState)
        val newShotsFired = currentPlayer.shotsFired + lastShot
        
        // Update data by creating new objects 
        val nextPlayerWithGridUpdated = nextPlayer.copyWithNewGrid(myGrid = newGrid)
        val newCurrentPlayer = currentPlayer.copyWithNewShotsFired(shotsFired = newShotsFired)

        // Check if game is over
        if(nextPlayerWithGridUpdated.myGrid.areAllShipsSunk()) {

            // Increase score of currentPlayer
            val newCurrentPlayerUpdated = newCurrentPlayer.copyWithNewScore(score = newCurrentPlayer.score + 1).copyWithNewShotsFired(shotsFired = newShotsFired)
            
            // Player1 saved as player1 (same for p2)
            val lastState = if(currentPlayer == state.player1) 
                    state.copy(player1 = newCurrentPlayerUpdated, player2 = nextPlayerWithGridUpdated, nbOfGames = state.nbOfGames + 1)
                else state.copy(player1 = nextPlayerWithGridUpdated, player2 = newCurrentPlayerUpdated, nbOfGames = state.nbOfGames + 1)

            return lastState
        }

        // Prepare for next turn
        if(currentPlayer == state.player1) 
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
            val newPlayer = p.askToPlaceAShip(ship)
            getNewPlayerWithShipsPlaced(ships.take(ships.length - 1), newPlayer)
        }        
    }

    /**
        Display the score of state players.
    */
    def outputFinalResult(state: GameState): Unit = {
        output.display(state.nbOfGames + " game(s) played.")
        output.display("===== SCORE =====\n" 
            + state.player1.name + ": " + state.player1.score 
            + "\n" + state.player2.name + ": " + state.player2.score 
        )   
    }

    /**
        Display a progress bar considering NB_OF_GAMES_TO_PLAY.
    */
    def outputNbOfGamesProgress(state: GameState): Unit = {
        val segmentsNb = 50
        val coeff = NB_OF_GAMES_TO_PLAY / segmentsNb

        val remainingGames = NB_OF_GAMES_TO_PLAY - state.nbOfGames
        val strPlayedMod100: String = "▉" * (state.nbOfGames / coeff)
        val strRemainingMod100: String = "░" * (segmentsNb - strPlayedMod100.length)
        
        if(remainingGames%coeff == 0) {
            output.clear()
            output.display(state.player1.name + " VS " + state.player2.name)
            output.display((NB_OF_GAMES_TO_PLAY - state.nbOfGames) + " remaining game(s) to play.")
            output.display("Progress: ")
            output.display(s"""$GREEN""" + strPlayedMod100 + strRemainingMod100 + s"""$RESET""")
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

    def createCSVRecap(states: Array[GameState]): Unit = {
        val header = "AI Name;score;AI Name2;score2\n"
        val fileName = "ai_proof.csv"
        val scores = states.map( state => {
            
            val (p1, p2) = (state.player1, state.player2)

            // First AI printed is the weakest one
            if(p1.isInstanceOf[AILow]) p1.name + ";" + p1.score + ";" + p2.name + ";" + p2.score
            else if(p2.isInstanceOf[AIMedium]) p2.name + ";" + p2.score + ";" + p1.name + ";" + p1.score
            else if(p2.isInstanceOf[AILow]) p2.name + ";" + p2.score + ";" + p1.name + ";" + p1.score
            else p1.name + ";" + p1.score + ";" + p2.name + ";" + p2.score  

        }).mkString("\n")

        // File writing
        val file = new File(fileName)
        val bw = new BufferedWriter(new FileWriter(file))
        bw.write(header.concat(scores))
        bw.close()  

        output.display("Recap file " + fileName + " written successfully!")      
    }
}