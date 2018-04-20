package pe.com.lycsoftware.game;

import java.io.Serializable;

import org.zkoss.zk.ui.Desktop;

public class GameUser
    implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 7202998643864819228L;
    private String userName;
    private GameRoom gameRoom;
    private Desktop desktop;
    private Integer score;

    public GameUser(GameRoom _gameRoom,
                    String _userName,
                    Desktop _desktop)
    {
        this.userName = _userName;
        this.gameRoom = _gameRoom;
        this.desktop = _desktop;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
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

    public void setGameRoom(GameRoom gameRoom)
    {
        this.gameRoom = gameRoom;
    }

    public Desktop getDesktop()
    {
        return desktop;
    }

    public void setDesktop(Desktop desktop)
    {
        this.desktop = desktop;
    }

}
