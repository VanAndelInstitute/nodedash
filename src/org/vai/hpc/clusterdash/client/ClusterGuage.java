package org.vai.hpc.clusterdash.client;

import com.google.gwt.core.client.GWT;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
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
	@UiField VerticalLayoutContainer vlc;
	final ListStore<Double> store = new ListStore<Double>(new ModelKeyProvider<Double>(){

		@Override
		public String getKey(Double item)
		{
			return item.toString();
		}});
	final GaugeSeries<Double> gauge = new GaugeSeries<Double>();
    final Chart<Double> chart = new Chart<Double>();
	 
	public ClusterGuage()
	{
		initWidget(uiBinder.createAndBindUi(this));
		vlc.setHeight(500);
		vlc.setWidth(500);
		
		
		store.add(55.0);
		final GaugeAxis<Double> axis = new GaugeAxis<Double>();
	    axis.setMargin(0);
	    axis.setDisplayGrid(false);
	    axis.setMinimum(0);
	    axis.setMaximum(100);
	    
	    TextSprite t = new TextSprite();
	    t.setHidden(true);
	      
	    

	   
	    gauge.addColor(new RGB("#005a9b"));
	    gauge.addColor(new RGB("#ddd"));
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

	   
	    chart.setHeight(500);
	    chart.setWidth(500);
	    chart.setStore(store);
	    chart.setAnimationDuration(1750);
	    chart.setAnimationEasing( new ElasticIn());
	    chart.setDefaultInsets(20);
	    chart.addAxis(axis);
	    chart.addSeries(gauge);
	    chart.setAnimated(true);
	    
	    
	    vlc.add(chart,new VerticalLayoutData(1.0, 0.5));
	}
	
	public void setValue(Double d)
	{
		store.clear();
		store.add(d);
		chart.redrawChart();
	}
}
