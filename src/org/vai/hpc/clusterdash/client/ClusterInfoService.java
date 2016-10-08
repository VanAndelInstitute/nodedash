package org.vai.hpc.clusterdash.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface ClusterInfoService extends RemoteService
{
	String greetServer(String name) throws IllegalArgumentException;
}