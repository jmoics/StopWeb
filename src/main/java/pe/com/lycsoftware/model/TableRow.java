package pe.com.lycsoftware.model;

import java.util.LinkedList;
import java.util.List;

public class TableRow
{
    private Integer turn;
    private List<TableCell> cells;
    
    public TableRow() {
        cells = new LinkedList<TableCell>();
    }
    
    public Integer getTurn()
    {
        return turn;
    }
    
    public void setTurn(Integer turn)
    {
        this.turn = turn;
    }
    
    public List<TableCell> getCells()
    {
        return cells;
    }
    
    public void setCells(List<TableCell> cells)
    {
        this.cells = cells;
    }
    
}
