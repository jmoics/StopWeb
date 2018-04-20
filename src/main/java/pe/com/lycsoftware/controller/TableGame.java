package pe.com.lycsoftware.controller;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Window;

public class TableGame
    extends SelectorComposer<Window>
{
    @Wire
    private Grid grdTableGame;
    
    @Override
    public void doAfterCompose(Window comp)
        throws Exception
    {
        // TODO Auto-generated method stub
        super.doAfterCompose(comp);
    }
}
