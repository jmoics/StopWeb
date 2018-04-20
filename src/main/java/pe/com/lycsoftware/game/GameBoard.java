package pe.com.lycsoftware.game;

public class GameBoard 
{
	private GameUser gameUser;
	private GameRoom gameRoom;
	
	public GameBoard(GameUser _gameUser,
					 GameRoom _gameRoom) {
		this.gameRoom = _gameRoom;
		this.gameUser = _gameUser;
	}

	public GameUser getGameUser() {
		return gameUser;
	}

	public GameRoom getGameRoom() {
		return gameRoom;
	}
	
	
}
