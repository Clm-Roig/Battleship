package battleship

case class GameState(
    val player1: Player,
    val player2: Player,
    val playerWhoBegins: Player
)