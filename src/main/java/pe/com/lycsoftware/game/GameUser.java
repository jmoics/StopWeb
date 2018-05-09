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

public class GameUser extends Thread
{
    /**
     * 
     */
    private static final long serialVersionUID = 7202998643864819228L;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameUser.class);
    private String userName;
    private GameRoom gameRoom;
    private final Desktop desktop;
    private Integer score;
    private boolean finishGame;
    //private boolean endTurn;
    private GameMessage gameMessage;

    public GameUser(GameRoom _gameRoom,
                    String _userName,
                    Desktop _desktop)
    {
        this.userName = _userName;
        this.gameRoom = _gameRoom;
        this.desktop = _desktop;
        this.gameMessage = null;
        this.gameRoom.add(this);
    }
    
    /**
     * Send new messages to UI if necessary.
     */
    public void run() {
        if (!this.desktop.isServerPushEnabled())
            this.desktop.enableServerPush(true);
        LOGGER.info("Active chatUser thread: " + getName());
        try {
            while (!finishGame) {
                try {
                    if (gameMessage == null) {
                        Threads.sleep(500);// Update each 0.5 seconds
                    } else {
                        Executions.activate(desktop);
                        try {
                            process();
                        } finally {
                            Executions.deactivate(desktop);
                        }
                    }
                } catch (DesktopUnavailableException ex) {
                    LOGGER.info("Browser exited.");
                    cleanUp();
                } catch (Throwable ex) {
                    LOGGER.error("Error in thread", ex);
                    throw UiException.Aide.wrap(ex);
                }
            }
        } finally {
            cleanUp();
        }
        LOGGER.info("chatUser thread ceased: " + getName() );
    }

    /**
     * Task: If there is a new message for the chat user, post a new "onBroadcast" event with
     * the message passed in.
     * @throws Exception
     */
    private void process() throws Exception {
        if (this.gameMessage.isEndTurn()) {
            LOGGER.info("processing turn: "+ this.gameMessage.getContent());
            /*HashMap<String, Message> msgs = new HashMap<String, Message>();
            msgs.put("msg",_msg);*/
            Events.postEvent(new Event("onBroadcast", this.desktop.getPage("mainPage").getFellow("winTabGam"), this.gameMessage));
            this.gameMessage = null;
        } else {
            LOGGER.info("processing message: "+ this.gameMessage.getContent());
            Events.postEvent(new Event("onBroadcast", this.desktop.getPage("mainPage").getFellow("winTabGam"), this.gameMessage));
            this.gameMessage = null;
        }
    }
    
    /**
     * Task: Clean up before stopping thread.
     */
    public void cleanUp(){
        LOGGER.info(getUserName() + " has logged out of the gameroom!");
        this.gameRoom.remove(this);
        if (desktop.isServerPushEnabled())
            Executions.getCurrent().getDesktop().enableServerPush(false);
        //setEndTurn();
    }

    public String getUserName()
    {
        return userName;
    }

    public Integer getScore()
    {
        return score;
    }

    public void setScore(Integer score)
    {
        this.score = score;
    }

    public GameRoom getGameRoom()
    {
        return gameRoom;
    }

    public Desktop getDesktop()
    {
        return desktop;
    }

    /*public void setEndTurn()
    {
        this.endTurn = true;
    }*/
    
    public void addGameMessage(GameMessage gameMessage)
    {
        this.gameMessage = gameMessage;
    }
    
}
