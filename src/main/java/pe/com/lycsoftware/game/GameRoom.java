package pe.com.lycsoftware.game;

import java.util.ArrayList;
import java.util.List;

public class GameRoom
{
    private List<GameUser> gameUsers;

    public GameRoom()
    {
        this.gameUsers = new ArrayList<GameUser>();
    }

    /**
     * Get a list of game users.
     * 
     * @return gameUsers
     */
    public List<GameUser> getGameUsers()
    {
        return this.gameUsers;
    }
    /**
     * Get a game user with a given username.
     * @param _username
     * @return
     */
    public GameUser getGameUser(String _username)
    {
        GameUser cu = null;
        synchronized (gameUsers) {
            for (GameUser gameUser : gameUsers)
                if (gameUser.getUserName().compareTo(_username) == 0) {
                    cu = gameUser;
                    break;
                }
        }
        return cu;
    }

    /**
     * Add a game user.
     * 
     * @param gameUser
     */
    public void add(GameUser gameUser)
    {
        synchronized (gameUsers) {
            gameUsers.add(gameUser);
        }
    }

    /**
     * Task: Remove a game user.
     * 
     * @param gameUser
     */
    public void remove(GameUser gameUser)
    {
        synchronized (gameUsers) {
            gameUsers.remove(gameUser);
        }
    }

    /**
     * Task: Remove a gameUser with a given nickname.
     * 
     * @param nickname
     */
    public void remove(String nickname)
    {
        synchronized (gameUsers) {
            gameUsers.remove(getGameUser(nickname));
        }
    }
}
