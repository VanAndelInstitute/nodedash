package org.vai.hpc.clusterdash.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.AreaSeries;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.fx.client.easing.ElasticIn;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

public class AreaChart extends Composite 
{

	private static ClusterGuageUiBinder uiBinder = GWT.create(ClusterGuageUiBinder.class);
    
	interface ClusterGuageUiBinder extends UiBinder<Widget, AreaChart>{}
	@UiField VerticalLayoutContainer chartPanel;
	@UiField Label header;
	@UiField Label footer;
	@UiField Label updateTimerText;
	int age=0;
	Boolean showChartLabel = false;
	Label chartText;
	Double chartMaximum;
	int offset = 0; 
	final ListStore<Double> store = new ListStore<Double>(new ModelKeyProvider<Double>(){
		@Override
		public String getKey(Double item)
		{
			return ""  + (offset++) + " " + item;
		}});
	final AreaSeries<Double> areaSeries = new AreaSeries<Double>();
    final Chart<Double> chart = new Chart<Double>();
	 
	public AreaChart(String header, String footer, Boolean showChartLabel,Double chartMaximum)
	{
		initWidget(uiBinder.createAndBindUi(this));
		this.header.setText(header);
		this.footer.setText(footer);
		this.showChartLabel = showChartLabel;
		this.chartMaximum = chartMaximum;
		PathSprite gridConfig = new PathSprite();
	    gridConfig.setStroke(new RGB("#bbb"));
	    gridConfig.setFill(new RGB("#ddd"));
	    gridConfig.setZIndex(1);
	    gridConfig.setStrokeWidth(1);
	    
	    
	    ValueProvider<Double,Double> doubleVP = new ValueProvider<Double,Double>(){

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
			}};
	    
	    
	      
		final NumericAxis<Double> axis = new NumericAxis<Double>();
		axis.setPosition(Position.LEFT);
		TextSprite t = new TextSprite();
		t.setFill(new RGB("#FFF"));
		axis.setLabelConfig(t);
	    axis.addField(doubleVP);
	    axis.setDisplayGrid(false);
	    if(chartMaximum > 1.0)
	    	axis.setMaximum(chartMaximum);
	    
	    areaSeries.setYAxisPosition(Position.LEFT);
	    areaSeries.addYField(doubleVP);	   
	    areaSeries.addColor(new RGB("#FFF"));

	    chart.setHeight(chartPanel.getOffsetHeight());
	    chart.setWidth(chartPanel.getOffsetWidth());
	    chart.setStore(store);
	    chart.setAnimationDuration(1750);
	    chart.setAnimationEasing( new ElasticIn());
	    chart.setDefaultInsets(1);
	    chart.addAxis(axis);
	    chart.addSeries(areaSeries);
	    chart.setBackground(new RGB("#005a9b"));
	    chart.setAnimated(true);
	    chartPanel.add(chart,new VerticalLayoutData(1.0, 1.0));
	    if(showChartLabel == true)
	    {
		    chartText = new Label(store.size() > 1 ? store.get(store.size()-1).toString() : "");
		    chartText.setStyleName("areaChartLabel");
		    chartPanel.add(chartText);
	    }
	    
	    
	    Timer updateTimer = new Timer(){
			@Override
			public void run() {
				age++;
				updateTimerText.setText("last updated " +  age + " seconds ago");
			}};
		updateTimer.scheduleRepeating(1000);

	    
	}
	
	public void addValue(Double d)
	{
		store.remove(0);
		store.add(d);
		chart.redrawChart();
		if(chartText != null)
			chartText.setText(d.toString());
		age=0;
	}
	
		
	public void addValue(Double d, String label)
	{
		store.remove(0);
		store.add(d);
		chart.redrawChart();
		if(chartText != null)
			chartText.setText(label);
		age=0;
	}
	
	public void preloadValue(Double d)
	{
		store.add(d);
		chart.redrawChart();
		age=0;
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
