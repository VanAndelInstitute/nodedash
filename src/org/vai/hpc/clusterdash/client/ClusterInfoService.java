package org.vai.hpc.clusterdash.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface ClusterInfoService extends RemoteService
{
	ArrayList<ClusterData> getCluster();
}
