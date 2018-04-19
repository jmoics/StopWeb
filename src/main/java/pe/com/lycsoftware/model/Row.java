package pe.com.lycsoftware.model;

import java.util.List;

public class Row
{
    private Integer turn;
    private List<Cell> cells;
    
    public Integer getTurn()
    {
        return turn;
    }
    
    public void setTurn(Integer turn)
    {
        this.turn = turn;
    }
    
    public List<Cell> getCells()
    {
        return cells;
    }
    
    public void setCells(List<Cell> cells)
    {
        this.cells = cells;
    }
    
}
