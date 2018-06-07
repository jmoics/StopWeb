package pe.com.lycsoftware.model;

import java.io.Serializable;

import pe.com.lycsoftware.game.GameUser;

public class TableCell
    implements Serializable
{
    private Category cat;
    private GameUser user;
    private String data;
    private Integer status;
    
    public TableCell(final Category _cat,
                     final String _data,
                     final GameUser _user)
    {
        this.cat = _cat;
        this.data = _data;
        this.user = _user;
    }
    
    public Category getCat()
    {
        return cat;
    }
    
    public void setCat(Category cat)
    {
        this.cat = cat;
    }
    
    public String getData()
    {
        return data;
    }
    
    public void setData(String data)
    {
        this.data = data;
    }
    
    public Integer getStatus()
    {
        return status;
    }
    
    public void setStatus(Integer status)
    {
        this.status = status;
    }
    
    public GameUser getUser()
    {
        return user;
    }
}