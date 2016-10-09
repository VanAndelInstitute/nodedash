package org.vai.hpc.clusterdash.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface ClusterInfoServiceAsync
{
	void getCluster(AsyncCallback<ArrayList<ClusterData>> callback);
}
