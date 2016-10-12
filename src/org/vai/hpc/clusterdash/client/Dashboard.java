package org.vai.hpc.clusterdash.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
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
	VerticalLayoutContainer vlc3 = new VerticalLayoutContainer();
	@UiField HorizontalLayoutContainer hlc;
	@UiField VerticalLayoutContainer main;
    Boolean preloadAreaChart = true;
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
		
		final ClusterGuage nodespct = new ClusterGuage("Node utilization", "Percent Nodes currently being used");
		final AreaChart loadavg = new AreaChart("Total CPU Load", "CPU Load over the last hour ",false);
		final TextMonitor runningJobs = new TextMonitor("Active Jobs", "The Number of currently Running Jobs");
		final TextMonitor allTimeJobs = new TextMonitor("Jobs Completed","Number of Jobs completed since July 2016");
		final WidgetMonitor topUsers = new WidgetMonitor("Top Active Users","The top active users online right now");
		final AreaChart diskrate = new AreaChart("Data Writes", "GigaBytes per minute over the last hour",true);
		
		vlc.add(nodespct);
		vlc.add(runningJobs);
		vlc2.add(allTimeJobs);
		vlc2.add(loadavg);
		vlc3.add(diskrate);
		vlc3.add(topUsers);
		hlc.add(vlc);
		hlc.add(vlc2);
		hlc.add(vlc3);
		
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
						int diskRateKB = 0;
						HashSet<Integer> jobs = new HashSet<Integer>();
						final HashMap<String,Integer> topUsersCnt = new HashMap<String,Integer>();
						
						for(ClusterData d : result)
						{
							try
							{
								totalLoad += d.getLoad();
								totalcores += d.getCoresAvail();
								diskRateKB = d.getKilobytesDiskActivtiy();
								
								if(d.getCoresUsed() == null || d.getJobIds() == null || d.getUsers() == null)
									continue;
								
								//for(int i : d.getCoresUsed())
								usedcores += d.coresAvail;
															
								for(int i : d.getJobIds())
								{
									lastJob = i > lastJob ? i : lastJob;
									jobs.add(i);
								}
								
								for(int i = 0; i< d.getUsers().size(); i++)
								{
									if(topUsersCnt.containsKey(d.getUsers().get(i)))
										topUsersCnt.put(d.getUsers().get(i),topUsersCnt.get(d.getUsers().get(i))+d.getCoresUsed().get(i));
									else
										topUsersCnt.put(d.getUsers().get(i),d.getCoresUsed().get(i));
								}
							}
							catch(Exception e) { }
						}
						
						nodespct.setValue(0.0 + (int)(100.0 * (0.0 + usedcores) / (0.0 + totalcores)));
						if(preloadAreaChart)
						{
							//preload the load history with last hour
							if(result.get(0) != null && result.get(0).getLoadHistory() != null && result.get(0).getLoadHistory().size() > 20)
							{
								for(Double d: result.get(0).getLoadHistory())
									loadavg.preloadValue(d);
							}
							else
							{
								for(int i = 0; i<60;i++)
									loadavg.preloadValue(totalLoad+i);
							}
							
							//preload the disk history with last hour
							if(result.get(0) != null && result.get(0).getDiskHistory() != null && result.get(0).getDiskHistory().size() > 20)
							{
								for(int d: result.get(0).getDiskHistory())
									diskrate.preloadValue(0.0 + d / 1000000.0);
							}
							else
							{
								for(int i = 0; i<60;i++)
									diskrate.preloadValue((0.0 + diskRateKB) / 1000000.0);
							}
							preloadAreaChart=false;
						}
						loadavg.addValue(totalLoad);
						diskrate.addValue((0.0 + diskRateKB) / 1000000.0, "" + (int)((0.0 + diskRateKB) / 1000000.0) + " GB/min");
						runningJobs.setMeasurement("" + jobs.size());
						allTimeJobs.setMeasurement("" + lastJob);
						
						//Create the top5 active users widget
						if(topUsersCnt.containsKey("hpc.admin"))
							topUsersCnt.remove("hpc.admin");
						ArrayList<String> sortedUsers = new ArrayList<String>();
						for(String s : topUsersCnt.keySet())
							sortedUsers.add(s);
						Collections.sort(sortedUsers, new Comparator<String>(){
							@Override
							public int compare(String o1, String o2)
							{
								return topUsersCnt.get(o2) - topUsersCnt.get(o1);
							}});

						VerticalLayoutContainer topusersPanel = new VerticalLayoutContainer(); 
						
						for (int i = 0; i<5 && i < sortedUsers.size();i++)
						{
							Label l = new Label((i+1) +  ". " + sortedUsers.get(i));
							l.setStylePrimaryName("topUsersEntry");
							topusersPanel.add(l);
						}
						topUsers.setContent(topusersPanel);
						
						
					}});
			}};
		t.run();
		t.scheduleRepeating(60000);
	}
}
