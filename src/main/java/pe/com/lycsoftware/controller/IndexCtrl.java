package pe.com.lycsoftware.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import pe.com.lycsoftware.game.GameBoard;
import pe.com.lycsoftware.game.GameMessage;
import pe.com.lycsoftware.game.GameRoom;
import pe.com.lycsoftware.game.GameUser;
import pe.com.lycsoftware.util.Constants;

public class IndexCtrl
    extends SelectorComposer<Window>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexCtrl.class);
    private static final String GAMEROOM_PREFIX = "game_room_";
    @Wire
    private Textbox txtName;
    @Wire
    private Textbox txtGameRoom;
    @Wire
    private Combobox cmbGameRoom;
    @Wire
    private Button btnLogin;
    @Wire
    private Grid loginGrid;
    @Wire
    private Hbox hboxButtons;
    @Wire
    private Vbox vboxLogin, vboxMain;
    @Wire
    private Row rowCreateRoom, rowJoinRoom;
    private GameRoom gameRoom;
    private GameUser gameUser;
    private String userName;
    private GameBoard gameBoard;

    @Override
    public void doAfterCompose(final Window comp)
        throws Exception
    {
        // TODO Auto-generated method stub
        super.doAfterCompose(comp);
    }

    @Listen("onClick = #btnLogin")
    public void login()
    {
        this.userName = this.txtName.getValue();
        String gameRoomName = this.rowCreateRoom.isVisible() 
                        ? GAMEROOM_PREFIX + this.txtGameRoom.getValue() : GAMEROOM_PREFIX + this.cmbGameRoom.getValue();
        this.gameRoom = (GameRoom) this.getSelf().getDesktop().getWebApp().getAttribute(gameRoomName);
        if (this.gameRoom == null) {
            this.gameRoom = new GameRoom(this.txtGameRoom.getValue());
            this.getSelf().getDesktop().getWebApp().setAttribute(gameRoomName, this.gameRoom);
        }
        // if username is not already being used
        if (this.gameRoom.getGameUser(this.userName) == null) {
            // initialize
            this.getSelf().getDesktop().enableServerPush(true);

            this.gameUser = new GameUser(this.gameRoom, this.userName, this.getSelf().getDesktop());
            // broadcast
            this.gameRoom.broadcast(new GameMessage(this.userName + " se a unido al juego", this.gameUser));
            this.gameUser.start();
            LOGGER.info("User: " + this.gameUser.getUserName() + " --> " + this.gameUser.getName());
            // set the MessageBoard to the session
            this.gameBoard = new GameBoard(this.gameUser, this.gameRoom);
            Sessions.getCurrent().setAttribute(Constants.GAMEBOARD_KEY, this.gameBoard);
            // welcome message
            final GameMessage msg = new GameMessage("Bienvenido al juego " + this.userName, this.gameUser);
            // refresh UI
            // displayChatGrid();
            final Map<String, GameMessage> map = new HashMap<>();
            map.put(Constants.INIT_MESSAGE, msg);
            Executions.createComponents("tableGame.zul", /*this.getSelf().getPage(),*/ null, map);
            //Executions.sendRedirect("tableGame.zul");
            this.vboxMain.setVisible(false);
            this.getSelf().detach();
            // appendMessage(msg);
        } else {
            alert("El nombre de usuario se encuentra en uso, por favor escoger otro");
        }
    }
    
    @Listen("onClick = #btnBack")
    public void back() {
        this.txtGameRoom.setVisible(true);
        this.cmbGameRoom.setVisible(false);
        this.hboxButtons.setVisible(true);
        this.vboxLogin.setVisible(false);
    }
    
    private void buildGameRoomCombo() {
        Map<String, Object> mapGameRooms = this.getSelf().getDesktop().getWebApp().getAttributes();
        for (Entry<String, Object> entry : mapGameRooms.entrySet()) {
            if (entry.getKey().startsWith(GAMEROOM_PREFIX)) {
                Comboitem item = new Comboitem();
                item.setValue(entry.getValue());
                item.setLabel(((GameRoom) entry.getValue()).getName());
                this.cmbGameRoom.appendChild(item);
            }
        }
    }
    
    @Listen("onClick = #btnCreateRoom")
    public void createRoom() {
        this.rowCreateRoom.setVisible(true);
        this.rowJoinRoom.setVisible(false);
        this.hboxButtons.setVisible(false);
        this.vboxLogin.setVisible(true);
    }
    
    @Listen("onClick = #btnJoinRoom")
    public void joinRoom() {
        this.rowCreateRoom.setVisible(false);
        this.rowJoinRoom.setVisible(true);
        this.hboxButtons.setVisible(false);
        this.vboxLogin.setVisible(true);
        buildGameRoomCombo();
    }
}
