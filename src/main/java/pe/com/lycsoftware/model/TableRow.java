package pe.com.lycsoftware.model;

import java.util.LinkedList;
import java.util.List;

public class TableRow
{
    private Integer turn;
    private List<TableCell> cells;
    private Integer total;
    
    public TableRow() {
        this.cells = new LinkedList<TableCell>();
        this.total = 0;
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
    
    public Integer getTotal()
    {
        return total;
    }
    
    public void setTotal(Integer total)
    {
        this.total = total;
    }
    
    public void calculate() 
    {
        Integer total = 0;
        for (TableCell cell : this.cells) {
            total = total + cell.getStatus();
        }
        this.total = total;
    }
}
