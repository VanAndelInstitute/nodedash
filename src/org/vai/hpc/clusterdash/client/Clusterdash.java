package org.vai.hpc.clusterdash.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.sencha.gxt.widget.core.client.container.Viewport;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Clusterdash implements EntryPoint
{
	
	public void onModuleLoad() 
	{
		Viewport viewport = new Viewport();
		Dashboard s = new Dashboard();
		viewport.setWidget(s);
		RootLayoutPanel.get().add(viewport);
		
	}
}
