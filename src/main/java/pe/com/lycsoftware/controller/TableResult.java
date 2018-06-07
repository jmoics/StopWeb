package pe.com.lycsoftware.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import pe.com.lycsoftware.game.GameBoard;
import pe.com.lycsoftware.game.GameUser;
import pe.com.lycsoftware.model.Category;
import pe.com.lycsoftware.model.TableCell;
import pe.com.lycsoftware.model.TableColumn;
import pe.com.lycsoftware.model.TableRow;
import pe.com.lycsoftware.util.Constants;

public class TableResult
    extends SelectorComposer<Window>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TableResult.class);
    @Wire
    private Grid grdTableResult;
    @Wire
    private Button btnRefresh;
    private GameBoard gameBoard;

    @Override
    public void doAfterCompose(final Window comp)
        throws Exception
    {
        super.doAfterCompose(comp);
        //this.gameBoard = (GameBoard) Sessions.getCurrent().getAttribute(Constants.GAMEBOARD_KEY);
        final Map<?, ?> mapArg = getSelf().getDesktop().getExecution().getArg();
        this.gameBoard = (GameBoard) mapArg.get(Constants.GAMEBOARD_KEY);

        calculate();
        buildGameBoard();
    }
    
    private synchronized void calculate()
    {
        if (!this.gameBoard.getGameRoom().isLastTurnCalculated()) {
            final Map<GameUser, TableRow> mapRes = this.gameBoard.getGameRoom().getResults()
                            .get(this.gameBoard.getGameRoom().getResults().size() - 1);
            
            while (mapRes.size() < this.gameBoard.getGameRoom().getGameUsers().size()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Map<Category, TableColumn> mapCalc;
            if (this.gameBoard.getGameRoom().getResultsCalc().size() < this.gameBoard.getGameRoom().getResults().size()) {
                mapCalc = new LinkedHashMap<>();
                this.gameBoard.getGameRoom().getResultsCalc().add(mapCalc);
            } else {
                mapCalc = this.gameBoard.getGameRoom().getResultsCalc()
                                .get(this.gameBoard.getGameRoom().getResults().size() - 1);
            }
            
            for (final Entry<GameUser, TableRow> entry : mapRes.entrySet()) {
                for (final TableCell tabcell : entry.getValue().getCells()) {
                    if (tabcell.getData().isEmpty() || tabcell.getData().length() == 1) {
                        tabcell.setData(null);
                        tabcell.setStatus(Constants.CELL_LOSE);
                    }
                    if (mapCalc.containsKey(tabcell.getCat())) {
                        TableColumn col = mapCalc.get(tabcell.getCat());
                        if (!col.getCells().containsKey(entry.getKey())) {
                            col.getCells().put(entry.getKey(), tabcell);
                        }
                    } else {
                        TableColumn col = new TableColumn();
                        col.getCells().put(entry.getKey(),tabcell);
                        mapCalc.put(tabcell.getCat(), col);
                    }
                }
            }
            
            for (Entry<Category, TableColumn> entry : mapCalc.entrySet()) {
                entry.getValue().calculate();
            }

            for (Entry<GameUser, TableRow> entry : mapRes.entrySet()) {
                entry.getValue().calculate();
                entry.getKey().setScore(entry.getKey().getScore() + entry.getValue().getTotal());
            }
            this.gameBoard.getGameRoom().setLastTurnCalculated(true);
        }
    }

    private void buildGameBoard()
    {
        if (this.grdTableResult.getRows().getChildren().size() == 0) {
            Column column = new Column("Jugador");
            column.setHflex("1");
            this.grdTableResult.getColumns().appendChild(column);
            for (final Category cat : this.gameBoard.getCategories()) {
                column = new Column(cat.getName());
                column.setHflex("2");
                this.grdTableResult.getColumns().appendChild(column);
            }
            column = new Column("Total");
            column.setHflex("1");
            this.grdTableResult.getColumns().appendChild(column);
        }

        final Map<GameUser, TableRow> map = this.gameBoard.getGameRoom().getResults()
                        .get(this.gameBoard.getGameRoom().getResults().size() - 1);
        for (final Entry<GameUser, TableRow> entry : map.entrySet()) {
            final Row row = new Row();
            Label label = new Label(entry.getKey().getUserName());
            row.appendChild(label);
            for (final TableCell tabcell : entry.getValue().getCells()) {
                Vbox vbox = new Vbox();
                label = new Label(tabcell.getData() == null 
                                || tabcell.getData().isEmpty() 
                                || tabcell.getData().length() == 1 
                                ? " - " : tabcell.getData());
                vbox.appendChild(label);
                label = new Label("" + tabcell.getStatus());
                vbox.appendChild(label);
                row.appendChild(vbox);
            }
            label = new Label("" + entry.getValue().getTotal());
            row.appendChild(label);
            this.grdTableResult.getRows().appendChild(row);
        }
    }

    @Listen("onClick = #btnRefresh")
    public void refresh() {
        this.grdTableResult.getColumns().getChildren().clear();
        this.grdTableResult.getRows().getChildren().clear();
        buildGameBoard();
    }
}
