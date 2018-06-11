package pe.com.lycsoftware.game;


public class GameMessage
{
    private String content;
    private String letter;
    private GameUser sender;
    private int status;
    
    public GameMessage(String _content, 
                       GameUser _sender){
        this.content = _content;
        this.sender = _sender;
        this.status = 2;
    }
    
    public GameMessage(String _content, 
                       GameUser _sender,
                       int _status,
                       String _letter){
        this.content = _content;
        this.sender = _sender;
        this.status = _status;
        this.letter = _letter;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public GameUser getSender()
    {
        return sender;
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public String getLetter()
    {
        return letter;
    }
}
