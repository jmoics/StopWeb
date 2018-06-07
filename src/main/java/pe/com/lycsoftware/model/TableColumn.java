package pe.com.lycsoftware.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import pe.com.lycsoftware.game.GameUser;
import pe.com.lycsoftware.util.Constants;

public class TableColumn
{
    private Integer turn;
    private final Map<GameUser,TableCell> cells;
    
    public TableColumn() {
        cells = new LinkedHashMap<GameUser,TableCell>();
    }

    public Integer getTurn()
    {
        return turn;
    }
    
    public void setTurn(Integer turn)
    {
        this.turn = turn;
    }
    
    public Map<GameUser,TableCell> getCells()
    {
        return cells;
    }

    public void calculate() 
    {
        for (Entry<GameUser,TableCell> entry : cells.entrySet()) {
            for (Entry<GameUser,TableCell> entry2 : cells.entrySet()) {
                if (!entry.getValue().equals(entry2.getValue())) {
                    if (entry2.getValue().getData().equalsIgnoreCase(entry.getValue().getData())) {
                        entry2.getValue().setStatus(Constants.CELL_DRAW);
                        entry.getValue().setStatus(Constants.CELL_DRAW);
                    } else {
                        if (entry.getValue().getStatus() == null && entry2.getValue().getStatus() == null) {
                            entry2.getValue().setStatus(Constants.CELL_WIN);
                            entry.getValue().setStatus(Constants.CELL_WIN);
                        } else if (entry.getValue().getStatus() == null && entry2.getValue().getStatus() != null) {
                            entry.getValue().setStatus(Constants.CELL_WIN);
                        } else if (entry2.getValue().getStatus() == null && entry.getValue().getStatus() != null) {
                            entry2.getValue().setStatus(Constants.CELL_WIN);
                        }
                    }
                }
            }
        }
    }
}
