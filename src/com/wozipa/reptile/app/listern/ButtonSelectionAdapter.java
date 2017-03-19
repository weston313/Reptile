package com.wozipa.reptile.app.listern;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

public class ButtonSelectionAdapter extends SelectionAdapter{
	
	public static final String BUTTON_ID="buttonId";
	
	private static ButtonSelectionAdapter adapter=null;
	
	public static ButtonSelectionAdapter getSelectionAdapter()
	{
		if(adapter==null)
		{
			synchronized (ButtonSelectionAdapter.class) {
				if(adapter==null)
				{
					adapter=new ButtonSelectionAdapter();
				}
			}
		}
		return adapter;
	}
	
	private ButtonSelectionAdapter() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		Button button=(Button) e.getSource();
		String buttonId=(String) button.getData(BUTTON_ID);
		switch (buttonId) {
		default:
			break;
		}
	}

}
