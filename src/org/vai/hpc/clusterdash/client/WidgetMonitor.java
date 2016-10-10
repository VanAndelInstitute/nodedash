package org.vai.hpc.clusterdash.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class WidgetMonitor extends Composite 
{

	private static ClusterGuageUiBinder uiBinder = GWT.create(ClusterGuageUiBinder.class);

	interface ClusterGuageUiBinder extends UiBinder<Widget, WidgetMonitor>{}
	@UiField VerticalLayoutContainer chartPanel;
	@UiField Label header;
	@UiField Label footer;
	@UiField Label updateTimerText;
	int age=0;
	final ListStore<Double> store = new ListStore<Double>(new ModelKeyProvider<Double>(){
		@Override
		public String getKey(Double item)
		{
			return item.toString();
		}});
	public WidgetMonitor(String header, String footer)
	{
		initWidget(uiBinder.createAndBindUi(this));
		this.header.setText(header);
		this.footer.setText(footer);
		Timer updateTimer = new Timer(){
 			@Override
 			public void run() {
 				age++;
 				updateTimerText.setText("last updated " +  age + " seconds ago");
 			}};
 			updateTimer.scheduleRepeating(1000);
	}
	
	public void setContent(Widget s)
	{
		age=0;
		chartPanel.clear();
		chartPanel.add(s);
	}
	public void setHeader(String s)
	{
		header.setText(s);
	}
	public void setFooter(String s)
	{
		footer.setText(s);
	}
}
