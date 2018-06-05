package pe.com.lycsoftware.game;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pe.com.lycsoftware.model.Category;
import pe.com.lycsoftware.model.TableRow;
import pe.com.lycsoftware.util.Constants;

public class GameBoard
{
    private GameUser gameUser;
    private GameRoom gameRoom;
    private List<TableRow> tableGame;
    private Set<Category> categories;
    public static final int MAX_NO_OF_GAMES = 20;

    public GameBoard(GameUser _gameUser,
                     GameRoom _gameRoom)
    {
        this.gameRoom = _gameRoom;
        this.gameUser = _gameUser;
        this.tableGame = new LinkedList<>();
        this.categories = new LinkedHashSet<>(Arrays.asList(Constants.categories));
    }

    public GameUser getGameUser()
    {
        return this.gameUser;
    }

    public GameRoom getGameRoom()
    {
        return this.gameRoom;
    }

    public List<TableRow> getTableGame()
    {
        return this.tableGame;
    }
    
    public Set<Category> getCategories()
    {
        return categories;
    }
    
    public void addCategories(String _category)
    {
        this.categories.add(new Category(_category));
    }
}
