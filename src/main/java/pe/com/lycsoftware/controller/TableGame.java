package pe.com.lycsoftware.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import pe.com.lycsoftware.game.GameBoard;
import pe.com.lycsoftware.game.GameMessage;
import pe.com.lycsoftware.model.Category;
import pe.com.lycsoftware.util.Constants;

public class TableGame
    extends SelectorComposer<Window>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TableGame.class);
    @Wire
    private Grid grdTableGame;
    private GameBoard gameBoard;
    @Wire
    private Button btnStart;
    @Wire
    private Button btnStop;
    @Wire
    private Listbox lstMessages;
    
    @Override
    public void doAfterCompose(Window comp)
        throws Exception
    {
        super.doAfterCompose(comp);
        gameBoard = (GameBoard) Sessions.getCurrent().getAttribute("gameBoard");
        this.getSelf().getDesktop().enableServerPush(true);
        this.getSelf().setTitle(" " + this.gameBoard.getGameRoom().getName() + " - " + this.gameBoard.getGameUser().getUserName());
        final Map<?, ?> mapArg = getSelf().getDesktop().getExecution().getArg();
        GameMessage gameMessage = (GameMessage) mapArg.get(Constants.INIT_MESSAGE);
        buildGameBoard();
        appendMessage(gameMessage);
    }

    private void buildGameBoard()
    {
        Column column = new Column("Turno/Letra");
        column.setHflex("1");
        this.grdTableGame.getColumns().appendChild(column);
        for (Category cat : this.gameBoard.getCategories()) {
            column = new Column(cat.getName());
            column.setHflex("2");
            this.grdTableGame.getColumns().appendChild(column);
        }
        Row row = new Row();
        if (this.grdTableGame.getRows().getChildren().size() == 0) {
            Label label = new Label("1");
            row.appendChild(label);
            for (Category cat : this.gameBoard.getCategories()) {
                Textbox textCat = new Textbox();
                textCat.setId(cat.getName() + "_1");
                textCat.setDisabled(true);
                row.appendChild(textCat);
            }
        }
        this.grdTableGame.getRows().appendChild(row);
    }
    
    private void rebuildGameBoard() {
        
    }
    
    /**
     * Handles the event fired when a message is broadcasted from another chat user.
     * @param event
     * @throws InterruptedException
     */
    @Listen("onBroadcast = #winTabGam")
    public void onBroadcast(Event event) {
        final GameMessage msg = (GameMessage) event.getData();
        //if a user is entering or leaving chatroom
        switch (msg.getStatus()) {
            case Constants.START_GAME:
                LOGGER.info("processing start turn: " + msg.getContent());
                if (this.grdTableGame.getRows().getChildren().size() > 0) {
                    Row row = (Row) this.grdTableGame.getRows().getLastChild();
                    for (Component comp : row.getChildren()) {
                        if (comp instanceof Textbox) {
                            ((Textbox) comp).setDisabled(false);
                        }
                    }
                }
                this.btnStart.setVisible(false);
                this.btnStop.setVisible(true);
                break;
            case Constants.STOP_GAME:
                LOGGER.info("processing end turn: " + msg.getContent());
                if (this.grdTableGame.getRows().getChildren().size() > 0) {
                    Row row = (Row) this.grdTableGame.getRows().getLastChild();
                    for (Component comp : row.getChildren()) {
                        if (comp instanceof Textbox) {
                            ((Textbox) comp).setDisabled(true);
                        }
                    }
                }
                this.btnStart.setVisible(true);
                this.btnStop.setVisible(false);
                break;
            case Constants.JOIN_GAME:
                LOGGER.info("joining game: " + msg.getContent());
                break;
            default:
                break;
        }
        appendMessage(msg);
    }
    
    @Listen("onClick = #btnStart")
    public void startGame(final MouseEvent _event) {
        GameMessage gameMessage = new GameMessage("Turno Iniciado", 
                        this.gameBoard.getGameUser().getUserName(), Constants.START_GAME);
        this.gameBoard.getGameRoom().broadcastAll(gameMessage);
    }
    
    @Listen("onClick = #btnStop")
    public void stopGame(final MouseEvent _event) {
        GameMessage gameMessage = new GameMessage("Turno Terminado", 
                        this.gameBoard.getGameUser().getUserName(), Constants.STOP_GAME);
        this.gameBoard.getGameRoom().broadcastAll(gameMessage);
    }
    
    public void appendMessage(GameMessage _message) {
        this.lstMessages.appendChild(new Listitem(_message.getSender() + " - " + _message.getContent()));
    }
}
