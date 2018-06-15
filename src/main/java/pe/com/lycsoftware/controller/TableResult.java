package pe.com.lycsoftware.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import pe.com.lycsoftware.game.GameBoard;
import pe.com.lycsoftware.game.GameMessage;
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
        // this.gameBoard = (GameBoard)
        // Sessions.getCurrent().getAttribute(Constants.GAMEBOARD_KEY);
        final Map<?, ?> mapArg = getSelf().getDesktop().getExecution().getArg();
        this.gameBoard = (GameBoard) mapArg.get(Constants.GAMEBOARD_KEY);

        calculate();
        buildGameBoard();
        this.btnRefresh.setFocus(true);
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
            if (this.gameBoard.getGameRoom().getResultsCalc().size() < this.gameBoard.getGameRoom().getResults()
                            .size()) {
                mapCalc = new LinkedHashMap<>();
                this.gameBoard.getGameRoom().getResultsCalc().add(mapCalc);
            } else {
                mapCalc = this.gameBoard.getGameRoom().getResultsCalc()
                                .get(this.gameBoard.getGameRoom().getResults().size() - 1);
            }

            for (final Entry<GameUser, TableRow> entry : mapRes.entrySet()) {
                for (final TableCell tabcell : entry.getValue().getCells()) {
                    if (tabcell.getData().isEmpty() || tabcell.getData().length() == 1) {
                        tabcell.setData("");
                        tabcell.setStatus(Constants.CELL_LOSE);
                    }
                    if (mapCalc.containsKey(tabcell.getCat())) {
                        TableColumn col = mapCalc.get(tabcell.getCat());
                        if (!col.getCells().containsKey(entry.getKey())) {
                            col.getCells().put(entry.getKey(), tabcell);
                        }
                    } else {
                        TableColumn col = new TableColumn();
                        col.getCells().put(entry.getKey(), tabcell);
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

    private synchronized void calculateTotal()
    {
        if (!this.gameBoard.getGameRoom().isLastUpdateCalculated()) {
            final Map<GameUser, TableRow> mapRes = this.gameBoard.getGameRoom().getResults()
                            .get(this.gameBoard.getGameRoom().getResults().size() - 1);

            for (Entry<GameUser, TableRow> entry : mapRes.entrySet()) {
                entry.getValue().calculate();
                entry.getKey().setScoreLastTurn(entry.getValue().getTotal());
            }
            this.gameBoard.getGameRoom().setLastUpdateCalculated(true);
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
                if (entry.getKey().equals(this.gameBoard.getGameUser())) {
                    Vbox vbox = new Vbox();
                    label = new Label(tabcell.getData() == null
                                    || tabcell.getData().isEmpty()
                                    || tabcell.getData().length() == 1
                                                    ? " - " : tabcell.getData());
                    vbox.appendChild(label);
                    label = new Label("" + tabcell.getStatus());
                    vbox.appendChild(label);
                    row.appendChild(vbox);
                } else {
                    Vbox vbox = new Vbox();
                    vbox.setPack("center");
                    vbox.setHflex("1");
                    label = new Label(tabcell.getData() == null
                                    || tabcell.getData().isEmpty()
                                    || tabcell.getData().length() == 1
                                                    ? " - " : tabcell.getData());
                    vbox.appendChild(label);
                    buildResultWithButtons(tabcell, vbox);
                    row.appendChild(vbox);
                }
            }
            label = new Label("" + entry.getValue().getTotal());
            row.appendChild(label);
            this.grdTableResult.getRows().appendChild(row);
        }
    }

    private void buildResultWithButtons(TableCell tabcell,
                                        Vbox vbox)
    {
        Hbox hbox = new Hbox();
        hbox.setHflex("1");
        Button btnLose = new Button();
        btnLose.setStyle("padding-left: 2px; padding-right: 2px;");
        btnLose.setLabel("" + Constants.CELL_LOSE);
        // btnLose.setImage("/media/error-16.png");
        btnLose.setHflex("1");
        ;
        btnLose.setDisabled(Constants.CELL_LOSE.equals(tabcell.getStatus()));
        hbox.appendChild(btnLose);
        Button btnDraw = new Button();
        btnDraw.setStyle("padding-left: 2px; padding-right: 2px;");
        btnDraw.setLabel("" + Constants.CELL_DRAW);
        // btnDraw.setImage("/media/draw-16.png");
        btnDraw.setHflex("1");
        ;
        btnDraw.setDisabled(Constants.CELL_DRAW.equals(tabcell.getStatus()));
        hbox.appendChild(btnDraw);
        Button btnWin = new Button();
        btnWin.setStyle("padding-left: 2px; padding-right: 2px;");
        btnWin.setLabel("" + Constants.CELL_WIN);
        // btnWin.setImage("/media/ok-16.png");
        btnWin.setHflex("1");
        ;
        btnWin.setDisabled(Constants.CELL_WIN.equals(tabcell.getStatus()));
        hbox.appendChild(btnWin);
        vbox.appendChild(hbox);
        btnLose.addEventListener(Events.ON_CLICK, new EventListener<Event>()
        {

            @Override
            public void onEvent(Event _event)
                throws Exception
            {
                ((Button) _event.getTarget()).setDisabled(true);
                btnDraw.setDisabled(false);
                btnWin.setDisabled(false);
                tabcell.setStatus(Constants.CELL_LOSE);
                GameMessage msg = new GameMessage("", gameBoard.getGameUser(),
                                Constants.RESULT_GAME, null);
                gameBoard.getGameRoom().setLastUpdateCalculated(false);
                gameBoard.getGameRoom().broadcastResultAll(msg);
            }
        });
        btnDraw.addEventListener(Events.ON_CLICK, new EventListener<Event>()
        {

            @Override
            public void onEvent(Event _event)
                throws Exception
            {
                ((Button) _event.getTarget()).setDisabled(true);
                btnLose.setDisabled(false);
                btnWin.setDisabled(false);
                tabcell.setStatus(Constants.CELL_DRAW);
                GameMessage msg = new GameMessage("", gameBoard.getGameUser(),
                                Constants.RESULT_GAME, null);
                gameBoard.getGameRoom().setLastUpdateCalculated(false);
                gameBoard.getGameRoom().broadcastResultAll(msg);
            }
        });
        btnWin.addEventListener(Events.ON_CLICK, new EventListener<Event>()
        {

            @Override
            public void onEvent(Event _event)
                throws Exception
            {
                ((Button) _event.getTarget()).setDisabled(true);
                btnDraw.setDisabled(false);
                btnLose.setDisabled(false);
                tabcell.setStatus(Constants.CELL_WIN);
                GameMessage msg = new GameMessage("", gameBoard.getGameUser(),
                                Constants.RESULT_GAME, null);
                gameBoard.getGameRoom().setLastUpdateCalculated(false);
                gameBoard.getGameRoom().broadcastResultAll(msg);
            }
        });
    }

    private void buildResultsWithMenu(TableCell tabcell,
                                      Vbox vbox)
    {
        Menubar menubar = new Menubar();
        Menu menuChoose = new Menu();
        menubar.appendChild(menuChoose);
        Menupopup menupop = new Menupopup();
        menuChoose.appendChild(menupop);
        menuChoose.setLabel(String.valueOf(tabcell.getStatus()));
        Menuitem itemLose = new Menuitem("" + Constants.CELL_LOSE);
        itemLose.setImage("/media/error-16.png");
        menuChoose.getMenupopup().appendChild(itemLose);
        Menuitem itemDraw = new Menuitem("" + Constants.CELL_DRAW);
        itemDraw.setImage("/media/draw-16.png");
        menuChoose.getMenupopup().appendChild(itemDraw);
        Menuitem itemWin = new Menuitem("" + Constants.CELL_WIN);
        itemWin.setImage("/media/ok-16.png");
        menuChoose.getMenupopup().appendChild(itemWin);
        itemLose.addEventListener(Events.ON_CLICK, new EventListener<Event>()
        {

            @Override
            public void onEvent(Event _event)
                throws Exception
            {
                tabcell.setStatus(Constants.CELL_LOSE);
                menuChoose.setLabel(String.valueOf(tabcell.getStatus()));
                GameMessage msg = new GameMessage("", gameBoard.getGameUser(),
                                Constants.RESULT_GAME, null);
                gameBoard.getGameRoom().setLastUpdateCalculated(false);
                gameBoard.getGameRoom().broadcastResultAll(msg);
            }
        });
        itemDraw.addEventListener(Events.ON_CLICK, new EventListener<Event>()
        {

            @Override
            public void onEvent(Event _event)
                throws Exception
            {
                tabcell.setStatus(Constants.CELL_DRAW);
                menuChoose.setLabel(String.valueOf(tabcell.getStatus()));
                GameMessage msg = new GameMessage("", gameBoard.getGameUser(),
                                Constants.RESULT_GAME, null);
                gameBoard.getGameRoom().setLastUpdateCalculated(false);
                gameBoard.getGameRoom().broadcastResultAll(msg);
            }
        });
        itemWin.addEventListener(Events.ON_CLICK, new EventListener<Event>()
        {

            @Override
            public void onEvent(Event _event)
                throws Exception
            {
                tabcell.setStatus(Constants.CELL_WIN);
                menuChoose.setLabel(String.valueOf(tabcell.getStatus()));
                GameMessage msg = new GameMessage("", gameBoard.getGameUser(),
                                Constants.RESULT_GAME, null);
                gameBoard.getGameRoom().setLastUpdateCalculated(false);
                gameBoard.getGameRoom().broadcastResultAll(msg);
            }
        });
        vbox.appendChild(menubar);
    }

    @Listen("onClick = #btnRefresh")
    public void refresh()
    {
        this.grdTableResult.getColumns().getChildren().clear();
        this.grdTableResult.getRows().getChildren().clear();
        calculateTotal();
        buildGameBoard();
    }

    @Listen("onBroadcastResult = #winTabRes")
    public void onBroadcastResult(final Event event)
    {
        refresh();
    }
}
