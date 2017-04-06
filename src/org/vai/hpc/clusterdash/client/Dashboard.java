package org.vai.hpc.clusterdash.client;

import java.util.ArrayList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class Dashboard extends Composite 
{

	private static DashboardUiBinder uiBinder = GWT.create(DashboardUiBinder.class);

	interface DashboardUiBinder extends UiBinder<Widget, Dashboard> {}
	private final ClusterInfoServiceAsync clusterService = GWT.create(ClusterInfoService.class);
	VerticalLayoutContainer vlc = new VerticalLayoutContainer();
	@UiField HorizontalLayoutContainer hlc;
	@UiField VerticalLayoutContainer main;
	public Dashboard()
	{
		initWidget(uiBinder.createAndBindUi(this));
		
		main.setWidth(com.google.gwt.user.client.Window.getClientWidth() - 10);
		main.setWidth(com.google.gwt.user.client.Window.getClientWidth() - 20);
		main.setScrollMode(ScrollMode.AUTO);
		
		com.google.gwt.user.client.Window.addResizeHandler(new ResizeHandler(){

			@Override
			public void onResize(ResizeEvent event)
			{
				main.setWidth(event.getWidth()-10);
				main.setHeight(event.getHeight()-20);
			}});
		
	
		final StackedBarChart quotas = new StackedBarChart("Storage: Percent Used", "Yellow = Primary, White = Secondary. (Groups under 50% not shown)",false,100.0);
		
		vlc.add(quotas);
		hlc.add(vlc);
		
		Timer t = new Timer(){

			@Override
			public void run()
			{
				clusterService.getQuota(new AsyncCallback<ArrayList<QuotaData>>(){

					@Override
					public void onFailure(Throwable caught)
					{
						
						
					}

					@Override
					public void onSuccess(ArrayList<QuotaData> result)
					{
						ArrayList<QuotaData> filtered = new ArrayList<QuotaData>();
						for(QuotaData d : result)
						{
							if(d.getHomeNormalized() + d.getScratchNormalized() > 49)
								filtered.add(d);
						}
						quotas.addValues(filtered);
					}});
			}};
		t.run();
		t.scheduleRepeating(60000);
	}
}
