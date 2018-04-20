package pe.com.lycsoftware.game;

import java.util.ArrayList;
import java.util.List;

public class GameRoom 
{
	private List<GameUser> gameUsers;
	
	public GameRoom() {
		this.gameUsers = new ArrayList<GameUser>();
	}

	public List<GameUser> getGameUsers() {
		return this.gameUsers;
	}
	
	public GameUser getGameUser(String _username) {
		GameUser cu = null;
		synchronized (gameUsers) {
			for (GameUser chatUser : gameUsers)
				if (chatUser.getName().compareTo(_username) == 0) {
					cu = chatUser;
					break;
				}
		}
		return cu;
	}
}
