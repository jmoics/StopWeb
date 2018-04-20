package pe.com.lycsoftware.controller;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import chat.ChatRoom;
import chat.ChatUser;
import chat.Message;
import chat.MessageBoard;
import pe.com.lycsoftware.game.GameRoom;
import pe.com.lycsoftware.game.GameUser;

public class IndexCtrl
	extends SelectorComposer<Window>
{
	@Wire
	private Textbox txtName;
	@Wire
	private Textbox txtGameRoom;
	private GameRoom gameRoom;
	private GameUser gameUser;
	private String userName;
	
	@Override
	public void doAfterCompose(Window comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
	}
	
	public void login() {
		this.userName = this.txtName.getValue();
		gameRoom = (GameRoom) this.getSelf().getDesktop().getWebApp().getAttribute(this.txtGameRoom.getValue());
		if (gameRoom == null) {
			gameRoom = new GameRoom();
			this.getSelf().getDesktop().getWebApp().setAttribute(this.txtGameRoom.getValue(), this.gameRoom);
		}
		//if username is not already being used
		if (gameRoom.getGameUser(userName) == null) {
			//initialize
			this.getSelf().getDesktop().enableServerPush(true);

			gameUser = new GameUser(gameRoom, userName, desktop, _IMEnabled);
			//broadcast
			gameRoom.broadcast(new Message(userName
					+ " has joined this chatroom", userName, true));
						_chatUser.start();
			//set the MessageBoard to the session
			_msgBoard = new MessageBoard(_chatUser, gameRoom);
			Sessions.getCurrent().setAttribute("msgBoard", _msgBoard);
			//welcome message
			Message msg = new Message("Welcome " + userName, userName, true);
			_msgBoard.setMessage(msg);
			//refresh UI
			displayChatGrid();
			appendMessage(msg);
		} else {
			alert("This username is already in use. Please choose another.");
		}

	}
}
