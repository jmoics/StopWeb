package pe.com.lycsoftware.game;

import java.util.ArrayList;
import java.util.List;

import pe.com.lycsoftware.model.Row;

public class GameBoard
{
    private GameUser gameUser;
    private GameRoom gameRoom;
    private List<Row> tableGame;
    public static final int MAX_NO_OF_GAMES = 20;

    public GameBoard(GameUser _gameUser,
                     GameRoom _gameRoom)
    {
        this.gameRoom = _gameRoom;
        this.gameUser = _gameUser;
        this.tableGame = new ArrayList<Row>();
    }

    public GameUser getGameUser()
    {
        return this.gameUser;
    }

    public GameRoom getGameRoom()
    {
        return this.gameRoom;
    }

    public List<Row> getTableGame()
    {
        return this.tableGame;
    }
}
