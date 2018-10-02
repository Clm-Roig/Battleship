package battleship

case class GameState(player1: Player, player2: Player, playerWhoBegins: Int, currentPlayer: Player) {

    def this(player1: Player, player2: Player, playerWhoBegins: Int) {
        this(player1, player2, playerWhoBegins, if(playerWhoBegins == 0) player1 else player2)
    }
}