package battleship

case class GameState(
    player1: Player,
    player2: Player,
    playerWhoBegins: Int,
    currentPlayer: Int
)