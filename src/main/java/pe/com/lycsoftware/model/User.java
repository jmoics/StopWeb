package pe.com.lycsoftware.model;

import java.io.Serializable;
import java.util.List;

public class User
    implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 7202998643864819228L;
    private String name;
    private Integer score;
    private List<Row> table;
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public Integer getScore()
    {
        return score;
    }
    
    public void setScore(Integer score)
    {
        this.score = score;
    }

    
    public List<Row> getTable()
    {
        return table;
    }

    
    public void setTable(List<Row> table)
    {
        this.table = table;
    }
    
}
