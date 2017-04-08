package org.vai.hpc.clusterdash.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.BarSeries;
import com.sencha.gxt.chart.client.chart.series.Series.LabelPosition;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelConfig;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelProvider;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextBaseline;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.fx.client.easing.ElasticIn;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

public class StackedBarChart extends Composite
{

	private static StackedBarChartUiBinder uiBinder = GWT.create(StackedBarChartUiBinder.class);
	interface StackedBarChartUiBinder extends UiBinder<Widget, StackedBarChart> { }
	@UiField VerticalLayoutContainer chartPanel;
	@UiField Label header;
	@UiField Label footer;
	@UiField Label updateTimerText;
	int age=0;
	Boolean showChartLabel = false;
	Label chartText;
	Double chartMaximum;
	int offset = 0; 
	
	  public interface DataPropertyAccess extends PropertyAccess<QuotaData> {
  	    ValueProvider<QuotaData, Float> scratchNormalized();
  	    ValueProvider<QuotaData, Float> homeNormalized();
  	    ValueProvider<QuotaData, Float> totalUsedNormalized();
  	    ValueProvider<QuotaData, String> fileset();
  	  ValueProvider<QuotaData, String> filesetFormatted();
  	  
  	    ModelKeyProvider<QuotaData> id();
  	  }
  
	private static final DataPropertyAccess dataAccess = GWT.create(DataPropertyAccess.class);
	
	final ListStore<QuotaData> store = new ListStore<QuotaData>(dataAccess.id());
    final Chart<QuotaData> chart = new Chart<QuotaData>();

	public StackedBarChart(String header, String footer, Boolean showChartLabel,Double chartMaximum)
	{
		initWidget(uiBinder.createAndBindUi(this));
		
		chartPanel.setHeight(com.google.gwt.user.client.Window.getClientHeight() - 125);
		com.google.gwt.user.client.Window.addResizeHandler(new ResizeHandler(){

			@Override
			public void onResize(ResizeEvent event)
			{
				chartPanel.setHeight(event.getHeight()-195);
				chartPanel.setWidth(event.getWidth());
			}});
		
		
		this.header.setText(header);
		this.footer.setText(footer);
		this.showChartLabel = showChartLabel;
		this.chartMaximum = chartMaximum;
		PathSprite gridConfig = new PathSprite();
	    gridConfig.setStroke(new RGB("#bbb"));
	    gridConfig.setFill(new RGB("#ddd"));
	    gridConfig.setZIndex(1);
	    gridConfig.setStrokeWidth(1);
	    
	    
	    final NumericAxis<QuotaData> axis = new NumericAxis<QuotaData>();
		axis.setPosition(Position.BOTTOM);
		TextSprite t = new TextSprite();
		t.setFill(new RGB("#FFF"));
		t.setFontSize(20);
		axis.setLabelConfig(t);
		axis.addField(dataAccess.totalUsedNormalized());
	    //axis.addField(dataAccess.homeNormalized());
	    //axis.addField(dataAccess.scratchNormalized());
	    axis.setDisplayGrid(true);
	    axis.setMaximum(100);
	    axis.setMinimum(0);
	    
	    CategoryAxis<QuotaData, String> catAxis = new CategoryAxis<QuotaData, String>();
	    catAxis.setPosition(Position.LEFT);
	    
	    catAxis.setField(dataAccess.filesetFormatted());
	    TextSprite c = new TextSprite();
		c.setFill(new RGB("#FFF"));
		c.setFontSize(20);
		c.setTextBaseline(TextBaseline.MIDDLE);
	    catAxis.setLabelConfig(c);
	    
	    SeriesLabelConfig<QuotaData> labelConfig = new SeriesLabelConfig<QuotaData>();
	    labelConfig.setLabelPosition(LabelPosition.END);
	    labelConfig.setLabelContrast(true);
	    labelConfig.setLabelProvider(new SeriesLabelProvider<QuotaData>(){

			@Override
			public String getLabel(QuotaData item, ValueProvider<? super QuotaData, ? extends Number> valueProvider)
			{
				float TB = 1024*1024*1024;
				return "Primary: " + NumberFormat.getFormat("#0.0").format((1.0 * (item.getHomeUsed() / TB))) + "TB / " +  NumberFormat.getFormat("#0.0").format(item.getHomeTotal() / TB) + "TB  --- " +
				"Secondary: " + NumberFormat.getFormat("#0.0").format((1.0 * (item.getScratchUsed() / TB))) + "TB  / " +  NumberFormat.getFormat("#0.0").format(item.getScratchTotal() / TB) + "TB";		
//				if(valueProvider == dataAccess.homeNormalized())
//					if(item.getHomeTotal() == 0)
//						return "";
//					else
//						return NumberFormat.getFormat("#0.0").format((1.0 * (item.getHomeUsed() / TB))) + "TB used / " + (item.getHomeTotal() / TB) + "TB total";
//				else
//					if(item.getScratchTotal() == 0)
//						return "";
//				else
//					return NumberFormat.getFormat("#0.0").format((1.0 * (item.getScratchUsed() / TB))) + "TB used / " + (item.getScratchTotal() / TB) + "TB total";
			}});
	    
	    final BarSeries<QuotaData> bar = new BarSeries<QuotaData>();
	    bar.setYAxisPosition(Position.BOTTOM);
	    bar.addYField(dataAccess.totalUsedNormalized());
	   // bar.addYField(dataAccess.homeNormalized());
	    //bar.addYField(dataAccess.scratchNormalized());
	    bar.addColor(new RGB("#FF4"));
	    //bar.addColor(new RGB("#FFF"));
	    //bar.setStacked(true);
	    bar.setHighlighting(true);
	    bar.setLabelConfig(labelConfig);
	    
	   
	    
	    

	    chart.setHeight(chartPanel.getOffsetHeight());
	    chart.setWidth(chartPanel.getOffsetWidth());
	    chart.setStore(store);
	    chart.setAnimationDuration(1750);
	    chart.setAnimationEasing( new ElasticIn());
	    chart.setDefaultInsets(1);
	    chart.addAxis(axis);
	    chart.addAxis(catAxis);
	    chart.addSeries(bar);
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
	public void addValues(ArrayList<QuotaData> data,int cutoff)
	{
		setFooter("(Groups under " + cutoff + "% not shown)");
		addValues(data);
	}
	public void addValues(ArrayList<QuotaData> data)
	{
		store.clear();
		store.addAll(data);
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
