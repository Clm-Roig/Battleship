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

    val output: Output = ConsoleOutput()

    start()

    /**
        Start the Battleship program.
    */
    def start(): Unit = {

        // Init
        output.display("====================")
        output.display("==== BATTLESHIP ====")
        output.display("====================")
        val gameType = askForGameType()

        // TODO : case depending on gameType

        output.display("Player 1 name:")
        val p1Name = scala.io.StdIn.readLine()
        val p1 = new Human(p1Name)
        output.display("Player 2 name:")
        val p2Name = scala.io.StdIn.readLine()
        val p2 = new Human(p2Name)

        val beginner = (new Random).nextInt(2)

        // TODO : Place the ships    
        output.display(p1.name + " place your ships!")
        println(SHIPS(0))

        output.display(p2.name + " place your ships!")

        
        // Launch the battle
        output.display("\nThe battle between " + p1.name + " & " + p2.name + " begins!")
        if (beginner == 0) output.display(p1.name + " starts.") else output.display(p2.name + " starts.")
        val state = GameState(new Human(p1.name), new Human(p2.name), beginner)
        gameLoop(state)
    }

    // Game loop ((turn after turn))
    def gameLoop(state: GameState) {
        
    }

    /**
        Ask the user the game type he wants: 
    */
    def askForGameType(): Int = {
        output.display("Choose your game type (integer only):")
        GAME_TYPES.zipWithIndex.foreach{ 
            case(x, i) => println(i + ". " + x)
        }
        val valueTyped = scala.io.StdIn.readLine()
        try {
            val intTyped = Integer.parseInt(valueTyped)
            if(intTyped < 0 || intTyped >= GAME_TYPES.length) askForGameType()
            else intTyped
        } catch {
            case e: NumberFormatException => askForGameType()
        }
    }
}