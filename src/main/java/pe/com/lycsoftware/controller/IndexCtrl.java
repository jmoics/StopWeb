package pe.com.lycsoftware.controller;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import pe.com.lycsoftware.game.GameBoard;
import pe.com.lycsoftware.game.GameMessage;
import pe.com.lycsoftware.game.GameRoom;
import pe.com.lycsoftware.game.GameUser;

public class IndexCtrl
    extends SelectorComposer<Window>
{
    @Wire
    private Textbox txtName;
    @Wire
    private Textbox txtGameRoom;
    @Wire
    private Button btnLogin;
    private GameRoom gameRoom;
    private GameUser gameUser;
    private String userName;
    private GameBoard gameBoard;

    @Override
    public void doAfterCompose(Window comp)
        throws Exception
    {
        // TODO Auto-generated method stub
        super.doAfterCompose(comp);
    }

    @Listen("onClick = #btnLogin")
    public void login()
    {
        this.userName = this.txtName.getValue();
        gameRoom = (GameRoom) this.getSelf().getDesktop().getWebApp().getAttribute(this.txtGameRoom.getValue());
        if (gameRoom == null) {
            gameRoom = new GameRoom(this.txtGameRoom.getValue());
            this.getSelf().getDesktop().getWebApp().setAttribute(this.txtGameRoom.getValue(), this.gameRoom);
        }
        // if username is not already being used
        if (gameRoom.getGameUser(userName) == null) {
            // initialize
            this.getSelf().getDesktop().enableServerPush(true);

            gameUser = new GameUser(gameRoom, userName, this.getSelf().getDesktop());
            // broadcast
            gameRoom.broadcast(new GameMessage(userName + " has joined this gameroom", userName)); 
            gameUser.start();
            // set the MessageBoard to the session
            gameBoard = new GameBoard(gameUser, gameRoom);
            Sessions.getCurrent().setAttribute("gameBoard", gameBoard);
            // welcome message
            //GameMessage msg = new GameMessage("Welcome " + userName, userName, true);
            //gameBoard.setMessage(msg);
            // refresh UI
            // displayChatGrid();
            Executions.sendRedirect("tableGame.zul");
            // appendMessage(msg);
        } else {
            alert("This username is already in use. Please choose another.");
        }

    }
}
