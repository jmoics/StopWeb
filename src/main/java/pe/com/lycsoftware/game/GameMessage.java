package pe.com.lycsoftware.game;


public class GameMessage
{
    private String content;
    private String sender;
    private boolean endTurn;
    
    public GameMessage(String _content, 
                       String _sender){
        this.content = _content;
        this.sender = _sender;
        this.endTurn = false;
    }
    
    public GameMessage(String _content, 
                       String _sender,
                       boolean _endTurn){
        this.content = _content;
        this.sender = _sender;
        this.endTurn = _endTurn;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public String getSender()
    {
        return sender;
    }
    
    public boolean isEndTurn()
    {
        return endTurn;
    }
}
