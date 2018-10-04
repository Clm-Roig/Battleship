package battleship

/**
    Game state representation.
    @param player1 Player
    @param player2 Player
    @param playerWhoBegins Int, 0 if player1 began, 1 if player2 did
    @param currentPlayer Player, player currently playing (i.e asking to shot)
    @param nbOfGames Int, number of games played
*/
case class GameState(player1: Player, player2: Player, playerWhoBegins: Int, currentPlayer: Player, nbOfGames: Int = 0) {

    /**
        If curentPlayer not provided, determines it by looking at playerWhoBegins.
    */
    def this(player1: Player, player2: Player, playerWhoBegins: Int) {
        this(player1, player2, playerWhoBegins, if(playerWhoBegins == 0) player1 else player2)
    }
}