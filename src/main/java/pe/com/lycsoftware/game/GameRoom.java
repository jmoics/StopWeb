package pe.com.lycsoftware.game;

import java.util.ArrayList;
import java.util.List;

public class GameRoom
{

    private final String name;
    private final List<GameUser> gameUsers;
    private boolean readyPlayers;
    private boolean inGame;

    public GameRoom(final String _name)
    {
        this.name = _name;
        this.gameUsers = new ArrayList<>();
    }

    /**
     * Task: Send messages to all chatUsers except sender.
     *
     * @param message
     */
    public void broadcast(final GameMessage msg)
    {
        synchronized (this.gameUsers) {
            for (final GameUser gameUser : this.gameUsers)
                if (gameUser.getUserName().compareTo(msg.getSender()) != 0) {
                    gameUser.addGameMessage(msg);
                }
        }
    }

    public void broadcastAll(final GameMessage msg)
    {
        synchronized (this.gameUsers) {
            for (final GameUser gameUser : this.gameUsers)
                gameUser.addGameMessage(msg);
        }
    }

    public boolean checkReady4All()
    {
        boolean ret = true;
        synchronized (this.gameUsers) {
            for (final GameUser gameUser : this.gameUsers) {
                ret = ret && gameUser.isReady();
            }
        }
        return ret;
    }

    public String getName()
    {
        return name;
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
     *
     * @param _username
     * @return
     */
    public GameUser getGameUser(final String _username)
    {
        GameUser cu = null;
        synchronized (gameUsers) {
            for (final GameUser gameUser : gameUsers)
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
    public void add(final GameUser gameUser)
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
    public void remove(final GameUser gameUser)
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
    public void remove(final String nickname)
    {
        synchronized (gameUsers) {
            gameUsers.remove(getGameUser(nickname));
        }
    }

    public boolean isReadyPlayers()
    {
        return readyPlayers;
    }

    public void setReadyPlayers(final boolean readyPlayers)
    {
        this.readyPlayers = readyPlayers;
    }
    
    public boolean isInGame()
    {
        return inGame;
    }
    
    public void setInGame(boolean inGame)
    {
        this.inGame = inGame;
    }
}
