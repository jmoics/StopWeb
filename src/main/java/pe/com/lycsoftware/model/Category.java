package pe.com.lycsoftware.model;

import java.io.Serializable;

public class Category
    implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 8481774395349450225L;
    private String name;
    
    public Category(String _name) {
        this.name = _name;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
}
