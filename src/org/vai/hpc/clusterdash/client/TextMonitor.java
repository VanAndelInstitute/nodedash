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

public class TextMonitor extends Composite 
{

	private static ClusterGuageUiBinder uiBinder = GWT.create(ClusterGuageUiBinder.class);

	interface ClusterGuageUiBinder extends UiBinder<Widget, TextMonitor>{}
	@UiField VerticalLayoutContainer chartPanel;
	@UiField Label measurement;
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

    Label chartText;
    Timer smoothTimer;
	public TextMonitor(String header, String footer)
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
	
	public void setMeasurement(String s)
	{
		age=0;
		measurement.setText(s);
	}
	public String getMeasurement()
	{
		return measurement.getText();
	}
	public void setHeader(String s)
	{
		header.setText(s);
	}
	public void setMeasurement(final int m,int timeSpanMS )
	{
		try{
			int currentValue = Integer.parseInt(getMeasurement());
			if(smoothTimer != null && smoothTimer.isRunning())
				smoothTimer.cancel();
			if(currentValue == 0)
			{
				setMeasurement("" + m);
				return;
			}
			final int difference = m - currentValue;
			int updateCycle = timeSpanMS / difference;
			smoothTimer = new Timer(){
				@Override
				public void run() {
					if(Integer.parseInt(getMeasurement())==m)
					{
						age=0;
						this.cancel();
						return;
					}
					setMeasurement("" + (Integer.parseInt(getMeasurement())+(difference>0?1:-1)));
				}};
			smoothTimer.scheduleRepeating(updateCycle);
			
		}catch(Exception e)
		{
			setMeasurement("" + m);
		}
	}
	
	public void setFooter(String s)
	{
		footer.setText(s);
	}
}
