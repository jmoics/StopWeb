package pe.com.lycsoftware.controller;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import pe.com.lycsoftware.game.GameBoard;
import pe.com.lycsoftware.game.GameMessage;
import pe.com.lycsoftware.game.GameRoom;
import pe.com.lycsoftware.game.GameUser;
import pe.com.lycsoftware.util.Constants;

public class IndexCtrl
    extends SelectorComposer<Window>
{
    @Wire
    private Textbox txtName;
    @Wire
    private Textbox txtGameRoom;
    @Wire
    private Button btnLogin;
    @Wire
    private Grid loginGrid;
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
            gameRoom.broadcast(new GameMessage(userName + " se a unido al juego", userName)); 
            gameUser.start();
            // set the MessageBoard to the session
            gameBoard = new GameBoard(gameUser, gameRoom);
            Sessions.getCurrent().setAttribute("gameBoard", gameBoard);
            // welcome message
            GameMessage msg = new GameMessage("Bienvenido al juego " + userName, userName);
            // refresh UI
            // displayChatGrid();
            //Executions.sendRedirect("tableGame.zul");
            Map<String, GameMessage> map = new HashMap<>();
            map.put(Constants.INIT_MESSAGE, msg);
            Executions.createComponents("tableGame.zul", null, map);
            this.loginGrid.setVisible(false);
            this.getSelf().setBorder(false);
            // appendMessage(msg);
        } else {
            alert("El nombre de usuario se encuentra en uso, por favor escoger otro");
        }

    }
}
