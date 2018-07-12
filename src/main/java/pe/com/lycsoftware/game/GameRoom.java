package pe.com.lycsoftware.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pe.com.lycsoftware.model.Category;
import pe.com.lycsoftware.model.TableColumn;
import pe.com.lycsoftware.model.TableRow;

public class GameRoom
{

    private final String name;
    private final List<GameUser> gameUsers;
    private final List<Map<GameUser, TableRow>> results;
    private final List<Map<Category, TableColumn>> resultsCalc;
    private boolean inGame;
    private boolean lastTurnCalculated;
    private boolean lastUpdateCalculated;
    private final String reviewType;

    public GameRoom(final String _name,
                    final String _reviewType)
    {
        this.name = _name;
        this.reviewType = _reviewType;
        this.gameUsers = new ArrayList<>();
        this.results = new LinkedList<>();
        this.resultsCalc = new LinkedList<>();
    }

    /**
     * Task: Send messages to all chatUsers except sender.
     *
     * @param message
     */
    public void broadcast(final GameMessage msg)
    {
        synchronized (this.gameUsers) {
            for (final GameUser gameUser : this.gameUsers) {
                if (gameUser.getUserName().compareTo(msg.getSender().getUserName()) != 0) {
                    gameUser.addGameMessage(msg);
                }
            }
        }
    }

    public void broadcastAll(final GameMessage msg)
    {
        synchronized (this.gameUsers) {
            for (final GameUser gameUser : this.gameUsers) {
                gameUser.addGameMessage(msg);
            }
        }
    }

    public void broadcastResult(final GameMessage msg)
    {
        synchronized (this.gameUsers) {
            for (final GameUser gameUser : this.gameUsers) {
                if (gameUser.getUserName().compareTo(msg.getSender().getUserName()) != 0) {
                    gameUser.addGameMessage(msg);
                }
            }
        }
    }

    public void broadcastResultAll(final GameMessage msg)
    {
        synchronized (this.gameUsers) {
            for (final GameUser gameUser : this.gameUsers) {
                gameUser.addGameMessage(msg);
            }
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

    public void notReady4All()
    {
        synchronized (this.gameUsers) {
            for (final GameUser gameUser : this.gameUsers) {
                gameUser.setReady(false);
            }
        }
    }

    public String getName()
    {
        return this.name;
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
        synchronized (this.gameUsers) {
            for (final GameUser gameUser : this.gameUsers) {
                if (gameUser.getUserName().compareTo(_username) == 0) {
                    cu = gameUser;
                    break;
                }
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
        synchronized (this.gameUsers) {
            this.gameUsers.add(gameUser);
        }
    }

    /**
     * Task: Remove a game user.
     *
     * @param gameUser
     */
    public void remove(final GameUser gameUser)
    {
        synchronized (this.gameUsers) {
            this.gameUsers.remove(gameUser);
        }
    }

    /**
     * Task: Remove a gameUser with a given nickname.
     *
     * @param nickname
     */
    public void remove(final String nickname)
    {
        synchronized (this.gameUsers) {
            this.gameUsers.remove(getGameUser(nickname));
        }
    }

    public boolean isInGame()
    {
        return this.inGame;
    }

    public void setInGame(final boolean inGame)
    {
        this.inGame = inGame;
    }

    public List<Map<GameUser, TableRow>> getResults()
    {
        return this.results;
    }

    public List<Map<Category, TableColumn>> getResultsCalc()
    {
        return this.resultsCalc;
    }

    public boolean isLastTurnCalculated()
    {
        return this.lastTurnCalculated;
    }

    public void setLastTurnCalculated(final boolean lastTurnCalculated)
    {
        this.lastTurnCalculated = lastTurnCalculated;
    }

    public boolean isLastUpdateCalculated()
    {
        return this.lastUpdateCalculated;
    }

    public void setLastUpdateCalculated(final boolean lastUpdateCalculated)
    {
        this.lastUpdateCalculated = lastUpdateCalculated;
    }

    public String getReviewType()
    {
        return this.reviewType;
    }

}
