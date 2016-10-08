package org.vai.hpc.clusterdash.client;

import com.google.gwt.core.client.GWT;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class Dashboard extends Composite 
{

	private static DashboardUiBinder uiBinder = GWT.create(DashboardUiBinder.class);

	interface DashboardUiBinder extends UiBinder<Widget, Dashboard> {}
	private final ClusterInfoServiceAsync clusterService = GWT.create(ClusterInfoService.class);
	 @UiField VerticalLayoutContainer vlc;

	public Dashboard()
	{
		initWidget(uiBinder.createAndBindUi(this));
		vlc.add(new ClusterGuage());
	}
}
