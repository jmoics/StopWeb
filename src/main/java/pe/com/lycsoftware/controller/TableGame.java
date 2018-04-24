package pe.com.lycsoftware.controller;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import pe.com.lycsoftware.game.GameBoard;
import pe.com.lycsoftware.model.Category;

public class TableGame
    extends SelectorComposer<Window>
{
    @Wire
    private Grid grdTableGame;
    private GameBoard gameBoard;
    private Button btnStart;
    private Button btnStop;
    
    @Override
    public void doAfterCompose(Window comp)
        throws Exception
    {
        super.doAfterCompose(comp);
        gameBoard = (GameBoard) Sessions.getCurrent().getAttribute("gameBoard");
        this.getSelf().setTitle(" " + this.gameBoard.getGameRoom().getName() + " - " + this.gameBoard.getGameUser().getUserName());
        buildGameBoard();
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
                row.appendChild(textCat);
            }
        }
        this.grdTableGame.getRows().appendChild(row);
    }
    
    @Listen("onClick = #btnStart")
    public void startGame(final MouseEvent _event) {
        this.btnStart.setVisible(false);
        this.btnStop.setVisible(true);
    }
    
    @Listen("onClick = #btnStart")
    public void stopGame(final MouseEvent _event) {
        this.btnStart.setVisible(false);
        this.btnStop.setVisible(true);
    }
}
