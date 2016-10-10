package org.vai.hpc.clusterdash.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.axis.GaugeAxis;
import com.sencha.gxt.chart.client.chart.series.GaugeSeries;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.fx.client.easing.ElasticIn;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

public class ClusterGuage extends Composite 
{

	private static ClusterGuageUiBinder uiBinder = GWT.create(ClusterGuageUiBinder.class);

	interface ClusterGuageUiBinder extends UiBinder<Widget, ClusterGuage>{}
	@UiField VerticalLayoutContainer chartPanel;
	final ListStore<Double> store = new ListStore<Double>(new ModelKeyProvider<Double>(){
		@Override
		public String getKey(Double item)
		{
			return item.toString();
		}});
	final GaugeSeries<Double> gauge = new GaugeSeries<Double>();
    final Chart<Double> chart = new Chart<Double>();
    Label chartText;
	@UiField Label header;
	@UiField Label footer;
	@UiField Label updateTimerText;
	int age=0;
	public ClusterGuage(String header, String footer)
	{
		initWidget(uiBinder.createAndBindUi(this));
		this.header.setText(header);
		this.footer.setText(footer);
		store.add(0.0);
		final GaugeAxis<Double> axis = new GaugeAxis<Double>();
	    axis.setMargin(0);
	    axis.setDisplayGrid(false);
	    axis.setMinimum(0);
	    axis.setMaximum(100);
	    TextSprite t = new TextSprite();
	    t.setHidden(true);
	    axis.setLabelConfig(t);  
	    	   
	    gauge.addColor(new RGB("#FFF"));
	    gauge.addColor(new RGB("#292929"));
	    gauge.setStrokeWidth(50);
	    
	    gauge.setAngleField(new ValueProvider<Double,Double>(){

			@Override
			public Double getValue(Double object)
			{
				return object;
			}

			@Override
			public void setValue(Double object, Double value)
			{
				object = value;
				
			}

			@Override
			public String getPath()
			{
				return "val";
			}});
	    gauge.setNeedle(false);
	    gauge.setDonut(30);

	   
	    chart.setHeight(chartPanel.getOffsetHeight());
	    chart.setWidth(chartPanel.getOffsetWidth());
	    chart.setStore(store);
	    chart.setAnimationDuration(1750);
	    chart.setAnimationEasing( new ElasticIn());
	    chart.setDefaultInsets(1);
	    chart.addAxis(axis);
	    chart.addSeries(gauge);
	    chart.setAnimated(true);
	    chart.setBackground(new RGB("#005a9b"));
	    chartPanel.add(chart,new VerticalLayoutData(1.0, 1.0));
	    chartText = new Label(store.get(0).toString());
	    chartText.setStyleName("gaugeChartLabel");
	    chartPanel.add(chartText);
	    setValue(55.0);
	    Timer updateTimer = new Timer(){
	 			@Override
	 			public void run() {
	 				age++;
	 				updateTimerText.setText("last updated " +  age + " seconds ago");
	 			}};
	 	updateTimer.scheduleRepeating(1000);
	}
	
	public void setValue(Double d)
	{
		age=0;
		store.clear();
		store.add(d);
		chart.redrawChart();
		if(chartText != null)
			chartText.setText(d.toString() + "%");
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
