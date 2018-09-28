package battleship

case class GameState(
    player1: Option[Player] = None,
    player2: Option[Player] = None,
    playerWhoBegins: Int = 0 
)