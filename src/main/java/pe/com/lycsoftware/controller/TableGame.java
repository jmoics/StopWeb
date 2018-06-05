package pe.com.lycsoftware.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import pe.com.lycsoftware.game.GameBoard;
import pe.com.lycsoftware.game.GameMessage;
import pe.com.lycsoftware.game.GameUser;
import pe.com.lycsoftware.model.Category;
import pe.com.lycsoftware.model.TableCell;
import pe.com.lycsoftware.model.TableRow;
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
    @Wire
    private Checkbox chbReady;
    @Wire
    private Popup popLog;

    @Override
    public void doAfterCompose(final Window comp)
        throws Exception
    {
        super.doAfterCompose(comp);
        this.gameBoard = (GameBoard) Sessions.getCurrent().getAttribute(Constants.GAMEBOARD_KEY);
        this.getSelf().getDesktop().enableServerPush(true);
        this.getSelf().setTitle(" " + this.gameBoard.getGameRoom().getName() + " - "
                        + this.gameBoard.getGameUser().getUserName());
        final Map<?, ?> mapArg = getSelf().getDesktop().getExecution().getArg();
        final GameMessage gameMessage = (GameMessage) mapArg.get(Constants.INIT_MESSAGE);
        buildGameBoard();
        appendMessage(gameMessage);
    }
    
    private void saveGameBoard()
    {
        if (this.grdTableGame.getRows().getChildren().size() > 0) {
            final Row row = (Row) this.grdTableGame.getRows().getLastChild();
            TableRow tabRow = new TableRow();
            tabRow.setTurn(this.grdTableGame.getRows().getChildren().size());
            for (final Component comp : row.getChildren()) {
                if (comp instanceof Textbox) {
                    Category cat = (Category) comp.getAttribute(Constants.CATEGORY_KEY);
                    TableCell cell = new TableCell(cat, ((Textbox) comp).getValue());
                    tabRow.getCells().add(cell);
                }
            }
            this.gameBoard.getTableGame().add(tabRow);
            Map<GameUser, TableRow> map;
            if (this.gameBoard.getGameRoom().getResults()
                            .get(this.gameBoard.getGameRoom().getResults().size() - 1) == null) {
                map = new LinkedHashMap<>();
                this.gameBoard.getGameRoom().getResults().add(map);
            } else {
                map = this.gameBoard.getGameRoom().getResults()
                                .get(this.gameBoard.getGameRoom().getResults().size() - 1);
            }
            map.put(this.gameBoard.getGameUser(),
                     this.gameBoard.getTableGame().get(this.gameBoard.getTableGame().size() - 1));
        }
    }

    private void buildGameBoard()
    {
        if (this.grdTableGame.getRows().getChildren().size() == 0) {
            Column column = new Column("Turno/Letra");
            column.setHflex("1");
            this.grdTableGame.getColumns().appendChild(column);
            for (final Category cat : this.gameBoard.getCategories()) {
                column = new Column(cat.getName());
                column.setHflex("2");
                this.grdTableGame.getColumns().appendChild(column);
            }
            column = new Column("Total");
            column.setHflex("1");
            this.grdTableGame.getColumns().appendChild(column);
        }
        final Row row = new Row();
        Label label = new Label("" + (this.grdTableGame.getRows().getChildren().size() + 1));
        label.setId("turnLetter_" + (this.grdTableGame.getRows().getChildren().size() + 1));
        row.appendChild(label);
        for (final Category cat : this.gameBoard.getCategories()) {
            final Textbox textCat = new Textbox();
            textCat.setAttribute(Constants.CATEGORY_KEY, cat);
            textCat.setHflex("1");
            textCat.setId(cat.getName() + "_" + (this.grdTableGame.getRows().getChildren().size() + 1));
            textCat.setDisabled(true);
            row.appendChild(textCat);
        }
        label = new Label();
        label.setId("total_" + (this.grdTableGame.getRows().getChildren().size() + 1));
        row.appendChild(label);
        this.grdTableGame.getRows().appendChild(row);
    }
    
    public void runWindowResult()
    {
        final Map<String, Object> dataArgs = new HashMap<>();
        //dataArgs.put(Constantes.ATTRIBUTE_PARENTFORM, this);
        final Window w = (Window) Executions.createComponents("tableResult.zul", null, dataArgs);
        w.setPage(this.getSelf().getPage());
        //w.setParent(wEAT);
        //w.doOverlapped();
        w.doModal();
        //w.doEmbedded();
    }

    /**
     * Handles the event fired when a message is broadcasted from another chat
     * user.
     *
     * @param event
     * @throws InterruptedException
     */
    @Listen("onBroadcast = #winTabGam")
    public void onBroadcast(final Event event)
    {
        final GameMessage msg = (GameMessage) event.getData();
        // if a user is entering or leaving chatroom
        switch (msg.getStatus()) {
            case Constants.START_GAME:
                LOGGER.info("[" + this.gameBoard.getGameUser().getUserName() + "]" + " --> processing start turn: " + msg.getContent());
                if (this.grdTableGame.getRows().getChildren().size() > 0) {
                    final Row row = (Row) this.grdTableGame.getRows().getLastChild();
                    for (final Component comp : row.getChildren()) {
                        if (comp instanceof Textbox) {
                            ((Textbox) comp).setDisabled(false);
                        } else if (comp instanceof Label && comp.getId().startsWith("turnLetter_")) {
                            // TODO save the used letters.
                            ((Label) comp).setValue(((Label) comp).getValue() + " - " + msg.getLetter());
                        }
                    }
                }
                this.btnStart.setVisible(false);
                this.btnStop.setVisible(true);
                break;
            case Constants.STOP_GAME:
                LOGGER.info("[" + this.gameBoard.getGameUser().getUserName() + "]" + " --> processing end turn: " + msg.getContent());
                this.btnStart.setVisible(true);
                this.btnStop.setVisible(false);
                if (this.grdTableGame.getRows().getChildren().size() > 0) {
                    final Row row = (Row) this.grdTableGame.getRows().getLastChild();
                    for (final Component comp : row.getChildren()) {
                        if (comp instanceof Textbox) {
                            ((Textbox) comp).setDisabled(true);
                        }
                    }
                }
                this.gameBoard.getGameUser().setReady(false);
                this.chbReady.setChecked(false);
                saveGameBoard();
                buildGameBoard();
                break;
            case Constants.JOIN_GAME:
                LOGGER.info("[" + this.gameBoard.getGameUser().getUserName() + "]" + " --> joining game: " + msg.getContent());
                break;
            case Constants.READY_GAME:
                LOGGER.info("[" + this.gameBoard.getGameUser().getUserName() + "]" + " --> user ready for game: " + msg.getContent());
                break;
            default:
                break;
        }
        appendMessage(msg);
    }

    @Listen("onClick = #btnStart")
    public void startGame(final MouseEvent _event)
    {
        if (this.gameBoard.getGameRoom().isReadyPlayers() && !this.gameBoard.getGameRoom().isInGame()) {
            final GameMessage gameMessage = new GameMessage("Turno Iniciado",
                            this.gameBoard.getGameUser().getUserName(), Constants.START_GAME, generateRandomLetter());
            this.gameBoard.getGameRoom().setInGame(true);
            this.gameBoard.getGameRoom().broadcastAll(gameMessage);
        } else {
            alert("Los usuarios no se encuentran listos para iniciar el juego");
        }
    }

    @Listen("onClick = #btnStop")
    public void stopGame(final MouseEvent _event)
    {
        final GameMessage gameMessage = new GameMessage("Turno Terminado",
                        this.gameBoard.getGameUser().getUserName(), Constants.STOP_GAME, null);
        // GameRoom gameRoom = (GameRoom)
        // this.getSelf().getDesktop().getWebApp().getAttribute(this.gameBoard.getGameRoom().getName());
        // gameRoom.setReadyPlayers(false);
        this.gameBoard.getGameRoom().setInGame(false);
        this.gameBoard.getGameRoom().setReadyPlayers(false);
        this.gameBoard.getGameRoom().broadcastAll(gameMessage);
    }
    
    @Listen("onClick = #btnLog")
    public void showLog(final MouseEvent _event)
    {
        this.popLog.open(this.getSelf(), "overlap");
    }
    
    public void logout(final MouseEvent _event) 
    {
        
    }

    @Listen("onCheck = #chbReady")
    public void readyGame(final Event _event)
    {
        this.gameBoard.getGameUser().setReady(true);
        final GameMessage gameMessage = new GameMessage(this.gameBoard.getGameUser().getUserName() + "se encuentra listo",
                        this.gameBoard.getGameUser().getUserName(), Constants.READY_GAME, null);
        this.gameBoard.getGameRoom().broadcast(gameMessage);
        if (this.gameBoard.getGameRoom().checkReady4All()) {
            this.gameBoard.getGameRoom().setReadyPlayers(true);
        }
    }

    public void appendMessage(final GameMessage _message)
    {
        this.lstMessages.appendChild(new Listitem(_message.getSender() + " - " + _message.getContent()));
    }

    private String generateRandomLetter()
    {
        final String letter = RandomStringUtils.randomAlphabetic(1);
        return letter.toUpperCase();
    }
}
