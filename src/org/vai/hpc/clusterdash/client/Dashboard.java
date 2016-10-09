package org.vai.hpc.clusterdash.client;

import java.util.ArrayList;
import java.util.HashSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dev.util.collect.HashMap;
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
	VerticalLayoutContainer vlc2 = new VerticalLayoutContainer();
	@UiField HorizontalLayoutContainer hlc;
	@UiField VerticalLayoutContainer main;
    Boolean preloadAreaChart = true;
	public Dashboard()
	{
		initWidget(uiBinder.createAndBindUi(this));
		
		main.setWidth(com.google.gwt.user.client.Window.getClientWidth() - 10);
		main.setWidth(com.google.gwt.user.client.Window.getClientWidth() - 20);
		main.setScrollMode(ScrollMode.AUTO);
		
//		vlc.setWidth(com.google.gwt.user.client.Window.getClientWidth() / 2);
//		vlc2.setWidth(com.google.gwt.user.client.Window.getClientWidth() / 2);
//		vlc.setHeight(com.google.gwt.user.client.Window.getClientHeight()-20);
//		vlc2.setHeight(com.google.gwt.user.client.Window.getClientHeight()-20);
//		vlc.setScrollMode(ScrollMode.AUTO);
//		vlc2.setScrollMode(ScrollMode.AUTO);
		
		com.google.gwt.user.client.Window.addResizeHandler(new ResizeHandler(){

			@Override
			public void onResize(ResizeEvent event)
			{
				vlc.setWidth(event.getWidth()-10);
				vlc.setHeight(event.getHeight()-20);
			}});
		
		final ClusterGuage nodespct = new ClusterGuage("Node utilization", "Percent Nodes currently being used");
		final AreaChart loadavg = new AreaChart("Total CPU Load", "Total calcuations being performed right now");
		final TextMonitor runningJobs = new TextMonitor("Active Jobs", "The Number of currently Running Jobs");
		final TextMonitor allTimeJobs = new TextMonitor("Jobs Completed","Number of Jobs completed since July 2016");
		
		vlc.add(nodespct);
		vlc.add(runningJobs);
		vlc2.add(allTimeJobs);
		vlc2.add(loadavg);
		hlc.add(vlc);
		hlc.add(vlc2);
		
		Timer t = new Timer(){

			@Override
			public void run()
			{
				clusterService.getCluster(new AsyncCallback<ArrayList<ClusterData>>(){

					@Override
					public void onFailure(Throwable caught)
					{
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(ArrayList<ClusterData> result)
					{
						int totalcores = 0;
						int usedcores = 0;
						double totalLoad = 0.0;
						int lastJob = 0;
						HashSet<Integer> jobs = new HashSet<Integer>();
						
						for(ClusterData d : result)
						{
							totalLoad += d.getLoad();
							totalcores += d.getCoresAvail();
							
							if(d.getCoresUsed() != null)
								//for(int i : d.getCoresUsed())
								usedcores += d.coresAvail;
							
							if(d.getJobIds() != null)
								for(int i : d.getJobIds())
								{
									lastJob = i > lastJob ? i : lastJob;
									jobs.add(i);
								}
						}
						
						nodespct.setValue(0.0 + (int)(100.0 * (0.0 + usedcores) / (0.0 + totalcores)));
						loadavg.addValue(totalLoad);
						if(preloadAreaChart)
						{
							for(int i = 0; i<20;i++)
								loadavg.addValue(totalLoad+i);
							preloadAreaChart=false;
						}		
						runningJobs.setValue("" + jobs.size());
						allTimeJobs.setValue("" + lastJob);
					}});
			}};
		t.run();
		t.scheduleRepeating(60000);
	}
}
