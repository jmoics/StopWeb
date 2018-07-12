package pe.com.lycsoftware.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.lang.Threads;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

import pe.com.lycsoftware.util.Constants;

public class GameUser
    extends Thread
{

    /**
     *
     */
    private static final long serialVersionUID = 7202998643864819228L;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameUser.class);
    private final String userName;
    private final GameRoom gameRoom;
    private final Desktop desktop;
    private Integer score;
    private Integer scoreLastTurn;
    private boolean finishGame;
    private boolean logout;
    private boolean ready;
    // private boolean endTurn;
    private GameMessage gameMessage;
    private boolean admin;

    public GameUser(final GameRoom _gameRoom,
                    final String _userName,
                    final Desktop _desktop,
                    final boolean _admin)
    {
        this.userName = _userName;
        this.gameRoom = _gameRoom;
        this.desktop = _desktop;
        this.gameMessage = null;
        this.gameRoom.add(this);
        this.score = 0;
        this.scoreLastTurn = 0;
        this.admin = _admin;
    }

    public GameUser(final GameRoom _gameRoom,
                    final String _userName,
                    final Desktop _desktop)
    {
        this.userName = _userName;
        this.gameRoom = _gameRoom;
        this.desktop = _desktop;
        this.gameMessage = null;
        this.gameRoom.add(this);
        this.score = 0;
        this.scoreLastTurn = 0;
    }

    /**
     * Send new messages to UI if necessary.
     */
    @Override
    public void run()
    {
        if (!this.desktop.isServerPushEnabled()) {
            this.desktop.enableServerPush(true);
        }
        LOGGER.info("Active chatUser thread: " + getName());
        // try {
        while (!this.finishGame || !this.logout) {
            try {
                if (this.gameMessage == null) {
                    Threads.sleep(100);// Update each 0.5 seconds
                } else {
                    Executions.activate(this.desktop);
                    try {
                        if (this.gameMessage.getStatus() == Constants.RESULT_GAME) {
                            processResult();
                        } else {
                            process();
                        }
                    } finally {
                        Executions.deactivate(this.desktop);
                    }
                }
            } catch (final DesktopUnavailableException ex) {
                LOGGER.info("Browser exited.");
                logout();
            } catch (final Throwable ex) {
                LOGGER.error("Error in thread", ex);
                throw UiException.Aide.wrap(ex);
            }
        }
        /*
         * } finally { cleanUp(); }
         */
        LOGGER.info("chatUser thread ceased: " + getName());
    }

    /**
     * Task: If there is a new message for the chat user, post a new
     * "onBroadcast" event with the message passed in.
     *
     * @throws Exception
     */
    private void process()
        throws Exception
    {
        Events.postEvent(new Event("onBroadcast", null, this.gameMessage));
        this.gameMessage = null;
    }

    private void processResult()
        throws Exception
    {
        Events.postEvent(new Event("onBroadcastResult", null, this.gameMessage));
        this.gameMessage = null;
    }

    public void logout()
    {
        LOGGER.info(getUserName() + " has logged out of the gameroom!");
        this.gameRoom.remove(this);
        this.gameMessage = null;
        if (this.desktop.isServerPushEnabled()) {
            Executions.getCurrent().getDesktop().enableServerPush(false);
        }
        this.logout = true;
    }

    public String getUserName()
    {
        return this.userName;
    }

    public Integer getScore()
    {
        return this.score;
    }

    public void setScore(final Integer score)
    {
        this.score = score;
    }

    public GameRoom getGameRoom()
    {
        return this.gameRoom;
    }

    public Desktop getDesktop()
    {
        return this.desktop;
    }

    public void addGameMessage(final GameMessage gameMessage)
    {
        this.gameMessage = gameMessage;
    }

    public boolean isReady()
    {
        return this.ready;
    }

    public void setReady(final boolean ready)
    {
        this.ready = ready;
    }

    public boolean isLogout()
    {
        return this.logout;
    }

    public void setLogout(final boolean logout)
    {
        this.logout = logout;
    }

    public Integer getScoreLastTurn()
    {
        return this.scoreLastTurn;
    }

    public void setScoreLastTurn(final Integer scoreLastTurn)
    {
        this.scoreLastTurn = scoreLastTurn;
    }

    public boolean isAdmin()
    {
        return this.admin;
    }

}
