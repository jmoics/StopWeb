package pe.com.lycsoftware.model;

import java.io.Serializable;

public class Cell
    implements Serializable
{
    private Category cat;
    private String data;
    private Integer status;
    
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
}
