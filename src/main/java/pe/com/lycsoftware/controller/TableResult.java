package pe.com.lycsoftware.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Window;

import pe.com.lycsoftware.game.GameBoard;
import pe.com.lycsoftware.util.Constants;

public class TableResult
    extends SelectorComposer<Window>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TableResult.class);
    @Wire
    private Grid grdTableResult;
    private GameBoard gameBoard;
    
    @Override
    public void doAfterCompose(final Window comp)
        throws Exception
    {
        super.doAfterCompose(comp);
        this.gameBoard = (GameBoard) Sessions.getCurrent().getAttribute(Constants.GAMEBOARD_KEY);
    }
}
